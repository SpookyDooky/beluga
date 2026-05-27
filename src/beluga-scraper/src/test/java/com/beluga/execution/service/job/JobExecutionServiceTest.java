package com.beluga.execution.service.job;

import com.beluga.execution.event.job.JobStartedEvent;
import com.beluga.execution.model.job.ExecutionConfiguration;
import com.beluga.execution.model.job.Job;
import com.beluga.execution.model.task.Task;
import com.beluga.execution.service.task.JobTaskQueue;
import com.beluga.execution.service.worker.Worker;
import com.beluga.execution.service.worker.WorkerOrchestrator;
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

import static com.beluga.execution.model.task.TaskStatus.PAUSED;
import static com.beluga.execution.model.task.TaskStatus.STOPPED;
import static org.instancio.Select.field;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class JobExecutionServiceTest {
	
	@Mock
	private ContextLogger logger;
	@Mock
	private JobTaskQueue jobTaskQueue;
	@Mock
	private ApplicationEventPublisher applicationEventPublisher;
	@Mock
	private TaskExecutionService taskExecutionService;
	@Mock
	private WorkerOrchestrator workerOrchestrator;
	
	@InjectMocks
	private JobExecutionService jobExecutionService;
	
	@Mock
	private Worker worker;
	
	@Captor
	private ArgumentCaptor<RateLimiter> rateLimiterArgumentCaptor;
	@Captor
	private ArgumentCaptor<JobStartedEvent> jobStartedEventArgumentCaptor;
	
	@Test
	void shouldStartJob() {
		final Job job = spy(
				Instancio.of(Job.class)
						.set(
								field(Job::getExecutionConfiguration),
								Instancio.of(ExecutionConfiguration.class)
										.set(field(ExecutionConfiguration::getWorkers), 1)
										.create()
						).create()
		);
		
		final List<Task> tasks = new ArrayList<>(job.getTasks());
		
		jobExecutionService.executeJob(job);
		
		assertTrue(job.getTasks().isEmpty());
		tasks.forEach(task -> {
			verify(jobTaskQueue).offerTask(task);
		});
		
		verify(workerOrchestrator).startWorkers(
				job.getId(),
				job.getExecutionConfiguration().getTasksPerSecond(),
				job.getExecutionConfiguration().getWorkers()
		);
		
		verify(applicationEventPublisher).publishEvent(jobStartedEventArgumentCaptor.capture());
		final JobStartedEvent jobStartedEvent = jobStartedEventArgumentCaptor.getValue();
		
		assertEquals(job.getJobDefinitionId(), jobStartedEvent.getJobDefinitionId());
		assertEquals(job.getId(), jobStartedEvent.getJobExecutionId());
	}
	
	@Test
	void shouldStopJob() {
		final Long jobId = 123L;
		final Collection<Task> tasks = List.of(Instancio.create(Task.class));
		when(jobTaskQueue.clearTasks(jobId)).thenReturn(tasks);
		
		jobExecutionService.stop(jobId);
		
		tasks.forEach(task -> {
			verify(taskExecutionService).setStatusById(task.getId(), STOPPED);
		});
	}
	
	@Test
	void shouldPauseJob() {
		final Long jobId = 123L;
		final Collection<Task> tasks = List.of(Instancio.create(Task.class));
		when(jobTaskQueue.clearTasks(jobId)).thenReturn(tasks);
		
		jobExecutionService.pause(jobId);
		
		tasks.forEach(task -> {
			verify(taskExecutionService).setStatusById(task.getId(), PAUSED);
		});
	}
}