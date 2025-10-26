package com.x.scrape.execution.service.job;

import com.google.common.util.concurrent.RateLimiter;
import com.x.scrape.logging.ContextLogger;
import com.x.scrape.model.job_definition.JobDefinition;
import com.x.scrape.model.job_definition.configuration.UrlConfiguration;
import com.x.scrape.model.job_definition.configuration.execution_configuration.ExecutionConfiguration;
import com.x.scrape.model.task.Task;
import com.x.scrape.execution.service.task.JobTaskQueue;
import com.x.scrape.execution.service.task.TaskFactory;
import com.x.scrape.execution.service.worker.Worker;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

/**
 * This service takes care of starting the correct amount of {@link Worker}'s for each {@link JobDefinition}.
 * Furthermore, for each {@link JobDefinition} it also places all tasks in the {@link JobTaskQueue}.
 */
@Service
public class JobExecutionService {
	
	private final ContextLogger logger;
	private final ApplicationContext applicationContext;
	private final TaskFactory taskFactory;
	private final JobTaskQueue jobTaskQueue;
	
	public JobExecutionService(final ContextLogger logger,
	                           final ApplicationContext applicationContext,
	                           final TaskFactory taskFactory,
	                           final JobTaskQueue jobTaskQueue) {
		this.logger = logger;
		this.applicationContext = applicationContext;
		this.taskFactory = taskFactory;
		this.jobTaskQueue = jobTaskQueue;
	}
	
	// Job definition comes in
	// Create new JobExecution
	// Persist JobDefinition
	// Create new Job with JobExecutionId
	public void executeJob(final JobDefinition jobDefinition) {
		logger.info("Executing job");
		
		final List<Task> tasks = createTasks(jobDefinition);
		tasks.forEach(jobTaskQueue::offerTask);
		
		jobDefinition.createJobFolders();
		
		final ExecutionConfiguration executionConfiguration = jobDefinition.getJobConfiguration().getExecutionConfiguration();
		final RateLimiter rateLimiter = RateLimiter.create((double) executionConfiguration.getTasksPerSecond());
		
		for (int i = 0; i < executionConfiguration.getWorkers(); i++) {
			final Worker worker = applicationContext.getBean(Worker.class);
			worker.init(
					jobDefinition.getUuid(),
					rateLimiter
			);
			
			new Thread(worker::start)
					.start();
		}
	}
	
	private List<Task> createTasks(final JobDefinition jobDefinition) {
		final UrlConfiguration urlConfiguration = jobDefinition.getJobConfiguration()
				.getUrlConfiguration();
		
		if (urlConfiguration.getUrlFile() == null) {
			return createTasksFromUrlList(jobDefinition, urlConfiguration.getUrls());
		} else {
			return createTasksFromUrlList(
					jobDefinition,
					readUrlsFromFile(urlConfiguration.getUrlFile())
			);
		}
	}
	
	private List<Task> createTasksFromUrlList(final JobDefinition jobDefinition,
	                                          final List<URL> urls) {
		return urls.stream()
				.map(url -> taskFactory.create(url, jobDefinition))
				.toList();
	}
	
	private List<URL> readUrlsFromFile(final String fileName) {
		try {
			return Files.readAllLines(Path.of(fileName))
					.stream()
					.map(this::createUrl)
					.toList();
		} catch (final IOException e) {
			throw new IllegalStateException(e);
		}
	}
	
	private URL createUrl(final String rawUrl) {
		try {
			return new URL(rawUrl);
		} catch (final MalformedURLException e) {
			throw new IllegalArgumentException(e);
		}
	}
}
