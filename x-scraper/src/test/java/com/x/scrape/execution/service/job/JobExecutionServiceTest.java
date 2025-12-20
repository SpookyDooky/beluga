package com.x.scrape.execution.service.job;

import com.google.common.util.concurrent.RateLimiter;
import com.x.scrape.execution.event.job.JobStartedEvent;
import com.x.scrape.execution.model.Job;
import com.x.scrape.execution.service.task.JobTaskQueue;
import com.x.scrape.execution.service.worker.Worker;
import com.x.scrape.execution.service.worker.WorkerOrchestrator;
import com.x.scrape.logging.ContextLogger;
import com.x.scrape.model.job_definition.configuration.execution_configuration.ExecutionConfiguration;
import com.x.scrape.model.task.Task;
import com.x.scrape.service.task.TaskExecutionService;
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

import static com.x.scrape.model.task.TaskStatus.PAUSED;
import static com.x.scrape.model.task.TaskStatus.STOPPED;
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
		final Job job = spy(Instancio.of(Job.class)
				.set(
						field(Job::getExecutionConfiguration),
						Instancio.of(ExecutionConfiguration.class)
								.set(field(ExecutionConfiguration::getWorkers), 1)
								.create()
				).create());
		
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