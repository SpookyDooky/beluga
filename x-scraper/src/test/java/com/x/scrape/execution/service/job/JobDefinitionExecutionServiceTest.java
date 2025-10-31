package com.x.scrape.execution.service.job;

import com.google.common.util.concurrent.RateLimiter;
import com.x.scrape.execution.model.Job;
import com.x.scrape.execution.service.task.JobTaskQueue;
import com.x.scrape.execution.service.worker.Worker;
import com.x.scrape.logging.ContextLogger;
import com.x.scrape.model.job_definition.configuration.execution_configuration.ExecutionConfiguration;
import org.instancio.Instancio;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationContext;

import static org.instancio.Select.field;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class JobDefinitionExecutionServiceTest {
	
	@Mock
	private ContextLogger logger;
	@Mock
	private ApplicationContext applicationContext;
	@Mock
	private JobTaskQueue jobTaskQueue;
	
	@InjectMocks
	private JobExecutionService jobExecutionService;
	
	@Mock
	private Worker worker;
	
	@Captor
	private ArgumentCaptor<RateLimiter> rateLimiterArgumentCaptor;
	
	@BeforeEach
	void setup() {
		when(applicationContext.getBean(Worker.class)).thenReturn(worker);
		doNothing().when(worker).start();
	}
	
	@Test
	void shouldStartJob() {
		final Job job = spy(Instancio.of(Job.class)
				.set(
						field(Job::getExecutionConfiguration),
						Instancio.of(ExecutionConfiguration.class)
								.set(field(ExecutionConfiguration::getWorkers), 1)
								.create()
				).create());
		
		doNothing().when(job).createJobFolders();
		
		jobExecutionService.executeJob(job);
		
		job.getTasks().forEach(task -> {
			verify(jobTaskQueue).offerTask(task);
		});
		verify(worker).init(eq(job.getId()), rateLimiterArgumentCaptor.capture());
		verify(worker, after(250)).start();
		
		final RateLimiter rateLimiter = rateLimiterArgumentCaptor.getValue();
		assertEquals(job.getExecutionConfiguration().getTasksPerSecond(), (int) rateLimiter.getRate());
	}
	
	@Test
	void shouldRemoveTasksFromJob() {
		Assertions.fail();
	}
}