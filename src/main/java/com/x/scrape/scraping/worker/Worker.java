package com.x.scrape.scraping.worker;

import com.x.scrape.http.HttpService;
import com.x.scrape.model.job.scraping_configuration.ScrapingConfiguration;
import com.x.scrape.model.task.Task;
import com.x.scrape.model.task.event.TaskCompletedEvent;
import com.x.scrape.model.task.event.TaskFailedEvent;
import com.x.scrape.scraping.DocumentScrapingService;
import com.x.scrape.scraping.task.JobTaskQueue;
import com.x.scrape.scraping.worker.event.WorkerFinishedEvent;
import com.x.scrape.scraping.worker.event.WorkerStartedEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jsoup.nodes.Document;
import org.springframework.context.ApplicationEventPublisher;

import java.net.URL;
import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class Worker {
	
	private final Logger logger = LogManager.getLogger();
	
	private final UUID jobId;
	private final JobTaskQueue jobTaskQueue;
	private final DocumentScrapingService documentScrapingService;
	private final HttpService httpService;
	private final ApplicationEventPublisher applicationEventPublisher;
	
	
	private Instant startTime;
	
	public Worker(final UUID jobId,
	              final JobTaskQueue jobTaskQueue,
	              final DocumentScrapingService documentScrapingService,
	              final HttpService httpService,
	              final ApplicationEventPublisher applicationEventPublisher) {
		this.jobId = jobId;
		this.jobTaskQueue = jobTaskQueue;
		this.documentScrapingService = documentScrapingService;
		this.httpService = httpService;
		this.applicationEventPublisher = applicationEventPublisher;
	}
	
	public void start() {
		applicationEventPublisher.publishEvent(new WorkerStartedEvent(jobId));
		startTime = Instant.now();
		
		while (!jobTaskQueue.isQueueEmpty(jobId)) {
			jobTaskQueue.pollTask(jobId)
					.ifPresent(this::executeTask);
		}
		
		finish();
	}
	
	private void executeTask(final Task task) {
		logger.info("Executing task.");
		try {
			final List<Map<String, Object>> scrapingResult = scrapePage(task.getUrl(), task.getScrapingConfiguration());
			logger.info("Finished " + task.getUrl().toString() + " found " + scrapingResult.size() + " results");
			applicationEventPublisher.publishEvent(new TaskCompletedEvent(jobId, task.getId(), scrapingResult));
		} catch (final Exception e) {
			logger.error("Task execution failed.");
			applicationEventPublisher.publishEvent(new TaskFailedEvent(jobId, task.getId()));
		}
	}
	
	private List<Map<String, Object>> scrapePage(final URL url,
	                                             final ScrapingConfiguration scrapingConfiguration) {
		final Document document = httpService.retrievePage(url)
				.orElseThrow(() -> new IllegalStateException("Failed to retrieve page " + url.toString() + "."));
		
		return documentScrapingService.scrapeDocument(
				document,
				scrapingConfiguration
		);
	}
	
	private void finish() {
		final long totalTime = Instant.now().toEpochMilli() - startTime.toEpochMilli();
		logger.info("Worker finished in: " + totalTime + "ms");
		
		applicationEventPublisher.publishEvent(new WorkerFinishedEvent(jobId));
	}
}
