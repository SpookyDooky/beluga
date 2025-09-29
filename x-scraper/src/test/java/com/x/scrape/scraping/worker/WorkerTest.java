package com.x.scrape.scraping.worker;

import com.x.scrape.http.HttpService;
import com.x.scrape.model.task.Task;
import com.x.scrape.model.task.event.TaskCompletedEvent;
import com.x.scrape.model.task.event.TaskFailedEvent;
import com.x.scrape.scraping.DomScrapingService;
import com.x.scrape.scraping.task.JobTaskQueue;
import com.x.scrape.scraping.worker.event.WorkerFinishedEvent;
import com.x.scrape.scraping.worker.event.WorkerStartedEvent;
import org.instancio.Instancio;
import org.jsoup.nodes.Document;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationEventPublisher;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class WorkerTest {

	@Mock
	private UUID jobId;
	@Mock
	private JobTaskQueue jobTaskQueue;
	@Mock
	private DomScrapingService domScrapingService;
	@Mock
	private HttpService httpService;
	@Mock
	private ApplicationEventPublisher applicationEventPublisher;
	
	@InjectMocks
	private Worker worker;
	
	@Test
	void shouldSendWorkerEvents() {
		when(jobTaskQueue.isQueueEmpty(jobId)).thenReturn(true);
		
		worker.start();
		
		final ArgumentCaptor<WorkerStartedEvent> workerStartedEventArgumentCaptor = ArgumentCaptor.forClass(WorkerStartedEvent.class);
		verify(applicationEventPublisher).publishEvent(workerStartedEventArgumentCaptor.capture());
		final WorkerStartedEvent workerStartedEvent = workerStartedEventArgumentCaptor.getValue();
		assertSame(jobId, workerStartedEvent.getJobId());
		
		final ArgumentCaptor<WorkerFinishedEvent> workerFinishedEventArgumentCaptor = ArgumentCaptor.forClass(WorkerFinishedEvent.class);
		verify(applicationEventPublisher).publishEvent(workerFinishedEventArgumentCaptor.capture());
		final WorkerFinishedEvent workerFinishedEvent = workerFinishedEventArgumentCaptor.getValue();
		assertSame(jobId, workerFinishedEvent.getJobId());
	}
	
	@Test
	void shouldExecuteTask() {
		when(jobTaskQueue.isQueueEmpty(jobId))
				.thenReturn(false)
				.thenReturn(true);
		
		final Task task = Instancio.create(Task.class);
		when(jobTaskQueue.pollTask(jobId)).thenReturn(Optional.of(task));
		
		final Document document = mock();
		when(httpService.retrievePageAsDocument(task.getUrl())).thenReturn(Optional.of(document));
		
		final List<Map<String, Object>> scrapeResult = mock();
		when(domScrapingService.scrape(task.getUrl(), task.getScrapingConfiguration()))
				.thenReturn(scrapeResult);
		
		worker.start();
		
		final ArgumentCaptor<TaskCompletedEvent> taskCompletedEventArgumentCaptor = ArgumentCaptor.forClass(TaskCompletedEvent.class);
		verify(applicationEventPublisher).publishEvent(taskCompletedEventArgumentCaptor.capture());
		
		final TaskCompletedEvent taskCompletedEvent = taskCompletedEventArgumentCaptor.getValue();
		assertSame(jobId, taskCompletedEvent.getJobId());
		assertSame(task.getId(), taskCompletedEvent.getTaskId());
		assertSame(scrapeResult, taskCompletedEvent.getResult());
	}
	
	@Test
	void shouldNotThrowExceptionWhenTaskExecutionFails() {
		when(jobTaskQueue.isQueueEmpty(jobId))
				.thenReturn(false)
				.thenReturn(true);
		
		final Task task = Instancio.create(Task.class);
		when(jobTaskQueue.pollTask(jobId)).thenReturn(Optional.of(task));
		when(httpService.retrievePageAsDocument(task.getUrl())).thenThrow(IllegalArgumentException.class);
		
		assertDoesNotThrow((() -> worker.start()));
		
		final ArgumentCaptor<TaskFailedEvent> taskFailedEventArgumentCaptor = ArgumentCaptor.forClass(TaskFailedEvent.class);
		verify(applicationEventPublisher).publishEvent(taskFailedEventArgumentCaptor.capture());
		
		final TaskFailedEvent taskFailedEvent = taskFailedEventArgumentCaptor.getValue();
		assertSame(jobId, taskFailedEvent.getJobId());
		assertSame(task.getId(), taskFailedEvent.getTaskId());
	}
}