package com.x.scrape.scraping.job;

import com.google.common.util.concurrent.RateLimiter;
import com.x.scrape.logging.ContextLogger;
import com.x.scrape.model.job.Job;
import com.x.scrape.model.job.UrlConfiguration;
import com.x.scrape.model.job.execution.ExecutionConfiguration;
import com.x.scrape.model.task.Task;
import com.x.scrape.scraping.task.JobTaskQueue;
import com.x.scrape.scraping.task.TaskFactory;
import com.x.scrape.scraping.worker.Worker;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

/**
 * This service takes care of starting the correct amount of {@link Worker}'s for each {@link Job}.
 * Furthermore, for each {@link Job} it also places all tasks in the {@link JobTaskQueue}.
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
	
	public void executeJob(final Job job) {
		logger.info("Executing job");
		
		final List<Task> tasks = createTasks(job);
		tasks.forEach(jobTaskQueue::offerTask);
		
		job.createJobFolders();
		
		final ExecutionConfiguration executionConfiguration = job.getJobConfiguration().getExecutionConfiguration();
		final RateLimiter rateLimiter = RateLimiter.create((double) executionConfiguration.getTasksPerSecond());
		
		for (int i = 0; i < executionConfiguration.getWorkers(); i++) {
			final Worker worker = applicationContext.getBean(Worker.class);
			worker.init(
					job.getId(),
					rateLimiter
			);
			
			new Thread(worker::start)
					.start();
		}
	}
	
	private List<Task> createTasks(final Job job) {
		final UrlConfiguration urlConfiguration = job.getJobConfiguration()
				.getUrlConfiguration();
		
		if (urlConfiguration.getUrlFile() == null) {
			return createTasksFromUrlList(job, urlConfiguration.getUrls());
		} else {
			return createTasksFromUrlList(
					job,
					readUrlsFromFile(urlConfiguration.getUrlFile())
			);
		}
	}
	
	private List<Task> createTasksFromUrlList(final Job job,
	                                          final List<URL> urls) {
		return urls.stream()
				.map(url -> taskFactory.create(url, job))
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
