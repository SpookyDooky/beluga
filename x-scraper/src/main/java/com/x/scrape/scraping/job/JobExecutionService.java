package com.x.scrape.scraping.job;

import com.x.scrape.http.HttpService;
import com.x.scrape.model.job.Job;
import com.x.scrape.model.job.UrlConfiguration;
import com.x.scrape.model.task.Task;
import com.x.scrape.scraping.DocumentScrapingService;
import com.x.scrape.scraping.task.JobTaskQueue;
import com.x.scrape.scraping.task.TaskFactory;
import com.x.scrape.scraping.worker.Worker;
import org.apache.logging.log4j.Logger;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

@Service
public class JobExecutionService {
	
	private final Logger logger;
	private final HttpService httpService;
	private final DocumentScrapingService documentScrapingService;
	private final TaskFactory taskFactory;
	private final JobTaskQueue jobTaskQueue;
	private final ApplicationEventPublisher applicationEventPublisher;
	
	public JobExecutionService(final Logger logger,
	                           final HttpService httpService,
	                           final DocumentScrapingService documentScrapingService,
	                           final TaskFactory taskFactory,
	                           final JobTaskQueue jobTaskQueue,
	                           final ApplicationEventPublisher applicationEventPublisher) {
		this.logger = logger;
		this.httpService = httpService;
		this.documentScrapingService = documentScrapingService;
		this.taskFactory = taskFactory;
		this.jobTaskQueue = jobTaskQueue;
		this.applicationEventPublisher = applicationEventPublisher;
	}
	
	public void executeJob(final Job job) {
		logger.info("Executing job");
		
		final List<Task> tasks = createTasks(job);
		tasks.forEach(jobTaskQueue::offerTask);
		
		for (int i = 0; i < 1; i++) {
			final Worker worker = new Worker(
					job.getId(),
					jobTaskQueue,
					documentScrapingService,
					httpService,
					applicationEventPublisher
			);
			
			new Thread(worker::start).start();
			;
		}
	}
	
	private List<Task> createTasks(final Job job) {
		final UrlConfiguration urlConfiguration = job.getUrlConfiguration();
		
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
