package com.x.scrape.scraping.worker;

import com.x.scrape.http.HttpService;
import com.x.scrape.logging.CloseableContext;
import com.x.scrape.logging.ContextKeys;
import com.x.scrape.logging.ContextLogger;
import com.x.scrape.model.job.scraping_configuration.ScrapingConfiguration;
import com.x.scrape.model.task.Task;
import com.x.scrape.model.task.event.TaskCompletedEvent;
import com.x.scrape.model.task.event.TaskFailedEvent;
import com.x.scrape.scraping.DocumentScrapingService;
import com.x.scrape.scraping.task.JobTaskQueue;
import com.x.scrape.scraping.worker.event.WorkerFinishedEvent;
import com.x.scrape.scraping.worker.event.WorkerStartedEvent;
import org.apache.logging.log4j.LogManager;
import org.jsoup.nodes.Document;
import org.springframework.context.ApplicationEventPublisher;

import java.net.URL;
import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static com.x.scrape.logging.ContextKeys.RESULTS;
import static com.x.scrape.logging.ContextKeys.TASK_ID;

public class Worker {
	
	private final ContextLogger logger = new ContextLogger(LogManager.getLogger());
	
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
		try (final CloseableContext ignored = logger.with("jobId", jobId.toString())) {
			logger.info("Worker starting.");
			Thread.sleep(5_000);
			
			applicationEventPublisher.publishEvent(new WorkerStartedEvent(jobId));
			startTime = Instant.now();
			
			while (!jobTaskQueue.isQueueEmpty(jobId)) {
				jobTaskQueue.pollTask(jobId)
						.ifPresent(this::executeTask);
			}
			
			finish();
		} catch (final InterruptedException e) {
			logger.error("Unexpected exception occurred in worker.", e);
			throw new IllegalStateException(e);
		}
	}
	
	private void executeTask(final Task task) {
		try (final CloseableContext context = logger.with(TASK_ID, task.getId().toString())) {
			context.put(ContextKeys.URL, task.getUrl().toString());
			logger.info("Executing task.");
			
			final List<Map<String, Object>> scrapingResult = scrapePage(task.getUrl(), task.getScrapingConfiguration());
			context.put(RESULTS, scrapingResult.size() + "");
			logger.info("Finished task.");
			
			applicationEventPublisher.publishEvent(new TaskCompletedEvent(jobId, task.getId(), scrapingResult));
		} catch (final Exception e) {
			logger.error("Task execution failed.", e);
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
