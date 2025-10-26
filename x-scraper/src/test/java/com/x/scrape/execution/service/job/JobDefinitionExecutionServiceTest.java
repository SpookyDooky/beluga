package com.x.scrape.execution.service.job;

import com.google.common.util.concurrent.RateLimiter;
import com.x.scrape.logging.ContextLogger;
import com.x.scrape.model.job_definition.configuration.JobConfiguration;
import com.x.scrape.model.job_definition.JobDefinition;
import com.x.scrape.model.job_definition.configuration.UrlConfiguration;
import com.x.scrape.model.job_definition.configuration.execution_configuration.ExecutionConfiguration;
import com.x.scrape.model.task.Task;
import com.x.scrape.execution.service.task.JobTaskQueue;
import com.x.scrape.execution.service.task.TaskFactory;
import com.x.scrape.execution.service.worker.Worker;
import org.instancio.Instancio;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationContext;

import java.net.URL;
import java.util.List;

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
	private TaskFactory taskFactory;
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
	void shouldStartJob() throws Exception {
		final URL taskUrl = new URL("https://not-existent-url.com");
		final JobDefinition jobDefinition = spy(Instancio.of(JobDefinition.class)
				.set(
						field(JobDefinition::getJobConfiguration),
						Instancio.of(JobConfiguration.class)
								.set(
										field(JobConfiguration::getExecutionConfiguration),
										Instancio.of(ExecutionConfiguration.class)
												.set(field(ExecutionConfiguration::getWorkers), 1)
												.create()
								)
								.set(
										field(JobConfiguration::getUrlConfiguration),
										Instancio.of(UrlConfiguration.class)
												.ignore(field(UrlConfiguration::getUrlFile))
												.set(
														field(UrlConfiguration::getUrls),
														List.of(taskUrl)
												).create()
								)
								.create()
				).create());
		doNothing().when(jobDefinition).createJobFolders();
		
		final Task task = mock();
		when(taskFactory.create(taskUrl, jobDefinition)).thenReturn(task);
		
		jobExecutionService.executeJob(jobDefinition);
		
		verify(jobTaskQueue).offerTask(task);
		verify(worker).init(eq(jobDefinition.getUuid()), rateLimiterArgumentCaptor.capture());
		verify(worker, after(250)).start();
		
		final RateLimiter rateLimiter = rateLimiterArgumentCaptor.getValue();
		assertEquals(jobDefinition.getJobConfiguration().getExecutionConfiguration().getTasksPerSecond(), (int) rateLimiter.getRate());
	}
	
	@Test
	void shouldStartJobWithUrlFile() throws Exception {
		final URL expectedTaskUrl = new URL("https://not-a-real-url.com");
		final String urlFilePath = JobDefinitionExecutionServiceTest.class
				.getResource("/test-files/url-file.txt")
				.getPath()
				.replaceFirst("/", "");
		
		final JobDefinition jobDefinition = spy(Instancio.of(JobDefinition.class)
				.set(
						field(JobDefinition::getJobConfiguration),
						Instancio.of(JobConfiguration.class)
								.set(
										field(JobConfiguration::getExecutionConfiguration),
										Instancio.of(ExecutionConfiguration.class)
												.set(field(ExecutionConfiguration::getWorkers), 1)
												.create()
								)
								.set(
										field(JobConfiguration::getUrlConfiguration),
										Instancio.of(UrlConfiguration.class)
												.ignore(field(UrlConfiguration::getUrls))
												.set(field(UrlConfiguration::getUrlFile), urlFilePath).create()
								)
								.create()
				).create());
		doNothing().when(jobDefinition).createJobFolders();
		
		final Task task = mock();
		when(taskFactory.create(eq(expectedTaskUrl), eq(jobDefinition))).thenReturn(task);
		
		jobExecutionService.executeJob(jobDefinition);
		
		verify(jobTaskQueue).offerTask(task);
		verify(worker).init(eq(jobDefinition.getUuid()), any());
		verify(worker, after(250)).start();
	}
}