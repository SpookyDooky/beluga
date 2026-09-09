package com.beluga.execution.service.job;

import com.beluga.execution.event.job.JobStartedEvent;
import com.beluga.execution.model.job.ExecutionConfiguration;
import com.beluga.execution.model.job.Job;
import com.beluga.execution.model.task.Task;
import com.beluga.execution.service.task.JobTaskQueue;
import com.beluga.execution.service.worker.Worker;
import com.beluga.logging.ContextLogger;
import com.beluga.service.task.TaskExecutionService;
import com.google.common.util.concurrent.RateLimiter;
import org.instancio.Instancio;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationEventPublisher;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

import static com.beluga.execution.model.task.TaskStatus.PAUSED;
import static com.beluga.execution.model.task.TaskStatus.STOPPED;
import static org.instancio.Select.field;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class JobExecutorServiceTest {
	
	@Mock
	private ContextLogger logger;
	@Mock
	private JobTaskQueue jobTaskQueue;
	@Mock
	private ApplicationEventPublisher applicationEventPublisher;
	@Mock
	private TaskExecutionService taskExecutionService;
	@Mock
	private Worker worker;

	@InjectMocks
	private JobExecutorService jobExecutorService;
	
	@Captor
	private ArgumentCaptor<RateLimiter> rateLimiterArgumentCaptor;
	@Captor
	private ArgumentCaptor<JobStartedEvent> jobStartedEventArgumentCaptor;
	
	@Test
	void shouldStartJob() throws Exception {
		final Job job = spy(
				Instancio.of(Job.class)
						.set(
								field(Job::getExecutionConfiguration),
								Instancio.of(ExecutionConfiguration.class)
										.set(field(ExecutionConfiguration::getTasksPerSecond), 1)
										.create()
						).create()
		);
		
		final List<Task> tasks = new ArrayList<>(job.getTasks());

		final Task mockedTask = mock();
		when(jobTaskQueue.pollTask(job.getId())).thenReturn(Optional.of(mockedTask));
		when(jobTaskQueue.isQueueEmpty(job.getId())).thenReturn(false, true);

		new Thread(() -> jobExecutorService.execute(job)).start();
		Thread.sleep(750);

		assertTrue(job.getTasks().isEmpty());
		tasks.forEach(task -> {
			verify(jobTaskQueue).offerTask(task);
		});
		
		verify(applicationEventPublisher).publishEvent(jobStartedEventArgumentCaptor.capture());
		final JobStartedEvent jobStartedEvent = jobStartedEventArgumentCaptor.getValue();
		
		assertEquals(job.getJobDefinitionId(), jobStartedEvent.getJobDefinitionId());
		assertEquals(job.getId(), jobStartedEvent.getJobExecutionId());

		verify(worker).execute(mockedTask);
	}
	
	@Test
	void shouldStopJob() {
		final Long jobId = 123L;
		final Collection<Task> tasks = List.of(Instancio.create(Task.class));
		when(jobTaskQueue.clearTasks(jobId)).thenReturn(tasks);
		
		jobExecutorService.stop(jobId);
		
		tasks.forEach(task -> {
			verify(taskExecutionService).setStatusById(task.getId(), STOPPED);
		});
	}
	
	@Test
	void shouldPauseJob() {
		final Long jobId = 123L;
		final Collection<Task> tasks = List.of(Instancio.create(Task.class));
		when(jobTaskQueue.clearTasks(jobId)).thenReturn(tasks);
		
		jobExecutorService.pause(jobId);
		
		tasks.forEach(task -> {
			verify(taskExecutionService).setStatusById(task.getId(), PAUSED);
		});
	}
}