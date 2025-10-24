package com.x.scrape.scraping.job;

import com.google.common.util.concurrent.RateLimiter;
import com.x.scrape.logging.ContextLogger;
import com.x.scrape.model.JobConfiguration;
import com.x.scrape.model.job.Job;
import com.x.scrape.model.job.UrlConfiguration;
import com.x.scrape.model.job.execution.ExecutionConfiguration;
import com.x.scrape.model.task.Task;
import com.x.scrape.scraping.task.JobTaskQueue;
import com.x.scrape.scraping.task.TaskFactory;
import com.x.scrape.scraping.worker.Worker;
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
class JobExecutionServiceTest {
	
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
		final Job job = spy(Instancio.of(Job.class)
				.set(
						field(Job::getJobConfiguration),
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
		doNothing().when(job).createJobFolders();
		
		final Task task = mock();
		when(taskFactory.create(taskUrl, job)).thenReturn(task);
		
		jobExecutionService.executeJob(job);
		
		verify(jobTaskQueue).offerTask(task);
		verify(worker).init(eq(job.getId()), rateLimiterArgumentCaptor.capture());
		verify(worker, after(250)).start();
		
		final RateLimiter rateLimiter = rateLimiterArgumentCaptor.getValue();
		assertEquals(job.getJobConfiguration().getExecutionConfiguration().getTasksPerSecond(), (int) rateLimiter.getRate());
	}
	
	@Test
	void shouldStartJobWithUrlFile() throws Exception {
		final URL expectedTaskUrl = new URL("https://not-a-real-url.com");
		final String urlFilePath = JobExecutionServiceTest.class
				.getResource("/test-files/url-file.txt")
				.getPath()
				.replaceFirst("/", "");
		
		final Job job = spy(Instancio.of(Job.class)
				.set(
						field(Job::getJobConfiguration),
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
		doNothing().when(job).createJobFolders();
		
		final Task task = mock();
		when(taskFactory.create(eq(expectedTaskUrl), eq(job))).thenReturn(task);
		
		jobExecutionService.executeJob(job);
		
		verify(jobTaskQueue).offerTask(task);
		verify(worker).init(eq(job.getId()), any());
		verify(worker, after(250)).start();
	}
}