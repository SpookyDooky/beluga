package com.x.scrape.scraping.worker;

import com.x.scrape.http.HttpService;
import com.x.scrape.model.job.scraping_configuration.ScrapingConfiguration;
import com.x.scrape.model.task.Task;
import com.x.scrape.scraping.DocumentScrapingService;
import com.x.scrape.scraping.task.JobTaskQueue;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jsoup.nodes.Document;

import java.net.URL;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class Worker {
	
	private final Logger logger = LogManager.getLogger();
	
	private final JobTaskQueue jobTaskQueue;
	private final DocumentScrapingService documentScrapingService;
	private final HttpService httpService;
	
	private final UUID jobId;
	
	public Worker(final UUID jobId,
	              final JobTaskQueue jobTaskQueue,
	              final DocumentScrapingService documentScrapingService,
	              final HttpService httpService) {
		this.jobId = jobId;
		this.jobTaskQueue = jobTaskQueue;
		this.documentScrapingService = documentScrapingService;
		this.httpService = httpService;
	}
	
	public void start() {
		while (!jobTaskQueue.isQueueEmpty(jobId)) {
			jobTaskQueue.pollTask(jobId)
					.ifPresentOrElse(
							this::executeTask,
							this::finish
					);
		}
	}
	
	private void executeTask(final Task task) {
		logger.info("Executing task.");
		final List<Map<String, Object>> scrapingResult = scrapePage(task.getUrl(), task.getScrapingConfiguration());
		logger.info("Finished " + task.getUrl().toString() + " found " + scrapingResult.size() + " results");
//		logger.error("Send event that a task has finished, with the task result");
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
		logger.info("Worker finished.");
		logger.error("Should send worker finished event");
	}
}
