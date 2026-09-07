package com.beluga.execution.service.worker;

import com.beluga.execution.event.task.task_result.TaskResultEvent;
import com.beluga.execution.model.task.Task;
import com.beluga.http.HttpService;
import com.beluga.logging.ContextLogger;
import com.beluga.scraping.ScrapingService;
import com.beluga.scraping.model.ScrapingResult;
import com.beluga.util.TimingService;
import org.instancio.Instancio;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationEventPublisher;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class WorkerTest {

	private final static Long JOB_ID = 123L;

	@Mock
	private ContextLogger logger;
	@Mock
	private ScrapingService scrapingService;
	@Mock
	private HttpService httpService;
	@Mock
	private ApplicationEventPublisher applicationEventPublisher;
	@Mock
	private TimingService timingService;
	
	@InjectMocks
	private Worker worker;

	@Mock
	private WorkerTaskCompletedCallback completedCallback;

	@Captor
	private ArgumentCaptor<TaskResultEvent> taskResultEventArgumentCaptor;

	@Test
	void shouldExecuteTask() {
		final Task task = Instancio.create(Task.class);

		final ScrapingResult scrapingResult = mock(RETURNS_DEEP_STUBS);
		when(scrapingService.scrape(task.getUrl(), task.getScrapingConfiguration())).thenReturn(scrapingResult);

		worker.execute(task);

		verify(applicationEventPublisher, times(2)).publishEvent(taskResultEventArgumentCaptor.capture());

		final TaskResultEvent taskResultEvent1 = taskResultEventArgumentCaptor.getAllValues().get(0);
		assertEquals("data.json", taskResultEvent1.getKey());
		assertEquals(scrapingResult.getResult(), taskResultEvent1.getPayload().getData());

		final TaskResultEvent taskResultEvent2 = taskResultEventArgumentCaptor.getAllValues().get(1);
		assertEquals("source.html", taskResultEvent2.getKey());
		assertEquals(scrapingResult.getRawPage(), taskResultEvent2.getPayload().getData());

		verify(completedCallback).complete(worker);
	}
}