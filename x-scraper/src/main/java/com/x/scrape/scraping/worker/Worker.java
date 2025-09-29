package com.x.scrape.scraping.worker;

import com.x.scrape.http.HttpService;
import com.x.scrape.logging.CloseableContext;
import com.x.scrape.logging.ContextKeys;
import com.x.scrape.logging.ContextLogger;
import com.x.scrape.model.job.scraping_configuration.ScrapingConfiguration;
import com.x.scrape.model.task.ImageDownloadTask;
import com.x.scrape.model.task.Task;
import com.x.scrape.model.task.event.TaskCompletedEvent;
import com.x.scrape.model.task.event.TaskFailedEvent;
import com.x.scrape.model.task.event.TaskImageDownloadCompletedEvent;
import com.x.scrape.scraping.DomScrapingService;
import com.x.scrape.scraping.task.JobTaskQueue;
import com.x.scrape.scraping.worker.event.WorkerFinishedEvent;
import com.x.scrape.scraping.worker.event.WorkerStartedEvent;
import org.apache.logging.log4j.LogManager;
import org.springframework.context.ApplicationEventPublisher;

import java.io.File;
import java.io.InputStream;
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
	private final DomScrapingService domScrapingService;
	private final HttpService httpService;
	private final ApplicationEventPublisher applicationEventPublisher;
	
	
	private Instant startTime;
	
	public Worker(final UUID jobId,
	              final JobTaskQueue jobTaskQueue,
	              final DomScrapingService domScrapingService,
	              final HttpService httpService,
	              final ApplicationEventPublisher applicationEventPublisher) {
		this.jobId = jobId;
		this.jobTaskQueue = jobTaskQueue;
		this.domScrapingService = domScrapingService;
		this.httpService = httpService;
		this.applicationEventPublisher = applicationEventPublisher;
	}
	
	public void start() {
		try (final CloseableContext ignored = logger.with("jobId", jobId.toString())) {
			logger.info("Worker starting.");
			Thread.sleep(1_000);
			
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
			
			createTaskResultFolder(task);
			
			final List<Map<String, Object>> scrapingResult = scrapePage(task.getUrl(), task.getScrapingConfiguration());
			context.put(RESULTS, scrapingResult.size() + "");
			
			downloadImages(task, scrapingResult);
			
			logger.info("Finished task.");
			
			applicationEventPublisher.publishEvent(new TaskCompletedEvent(jobId, task.getId(), scrapingResult));
		} catch (final Exception e) {
			logger.error("Task execution failed.", e);
			applicationEventPublisher.publishEvent(new TaskFailedEvent(jobId, task.getId()));
		}
	}
	
	private void createTaskResultFolder(final Task task) {
		final File file = new File(task.getJob().getJobTaskResultsFolder() + "/" + task.getId());
		file.mkdirs();
	}
	
	private List<Map<String, Object>> scrapePage(final URL url,
	                                             final ScrapingConfiguration scrapingConfiguration) {
		return domScrapingService.scrape(
				url,
				scrapingConfiguration
		);
	}
	
	// TODO offer this as a task too so that rate limiting can be applied properly in the future
	// All though i have to wonder if the rate limit would ever be reached
	// So for now maybe the current way is fine
	private void downloadImages(final Task task,
	                            final List<Map<String, Object>> scrapedData) {
		scrapedData.forEach(elementScrapedData -> {
			final List<ImageDownloadTask> imageDownloadTasks = task.getImageDownloadsTask(elementScrapedData);
			
			for (final ImageDownloadTask imageDownloadTask : imageDownloadTasks) {
				final String fileName = UUID.randomUUID() + ".png";
				downloadImage(task, imageDownloadTask, fileName);
				final String filePath = task.getId() + "/images/" + fileName;
				
				addImagePathToResult(elementScrapedData, filePath, imageDownloadTask.getPropertyName());
			}
		});
	}
	
	/**
	 * Downloads an image and publishes an event to save the image.
	 *
	 * @param task task.
	 * @param imageDownloadTask image download sub-task.
	 * @param fileName name to save the image under.
	 */
	private void downloadImage(final Task task,
	                             final ImageDownloadTask imageDownloadTask,
	                             final String fileName) {
		logger.info("Downloading image");
		final InputStream imageInputStream = httpService.get(imageDownloadTask.getUrl());
		
		final TaskImageDownloadCompletedEvent event = new TaskImageDownloadCompletedEvent(jobId, task.getId(), fileName);
		event.setFileStream(imageInputStream);
		
		applicationEventPublisher.publishEvent(event);
	}
	
	private void addImagePathToResult(final Map<String, Object> scrapedData,
	                                  final String filePath,
	                                  final String propertyName) {
		final String urlValue = (String) scrapedData.get(propertyName);
		scrapedData.remove(propertyName);
		
		scrapedData.put(propertyName + ".url", urlValue);
		scrapedData.put(propertyName + ".imagePath", filePath);
	}
	
	private void finish() {
		final long totalTime = Instant.now().toEpochMilli() - startTime.toEpochMilli();
		logger.info("Worker finished in: " + totalTime + "ms");
		
		applicationEventPublisher.publishEvent(new WorkerFinishedEvent(jobId));
	}
}
