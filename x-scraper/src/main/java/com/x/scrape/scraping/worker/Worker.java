package com.x.scrape.scraping.worker;

import com.x.scrape.http.HttpService;
import com.x.scrape.logging.CloseableContext;
import com.x.scrape.logging.ContextKeys;
import com.x.scrape.logging.ContextLogger;
import com.x.scrape.model.event.storable.payload.ImagePayload;
import com.x.scrape.model.event.storable.payload.JsonPayload;
import com.x.scrape.model.event.storable.payload.StringPayload;
import com.x.scrape.model.job.Job;
import com.x.scrape.model.task.ImageDownloadTask;
import com.x.scrape.model.task.Task;
import com.x.scrape.model.task.event.TaskFailedEvent;
import com.x.scrape.model.task.event.task_result.StorageHint;
import com.x.scrape.model.task.event.task_result.TaskResultEvent;
import com.x.scrape.scraping.ScrapingService;
import com.x.scrape.scraping.model.ScrapingResult;
import com.x.scrape.scraping.task.JobTaskQueue;
import com.x.scrape.scraping.worker.event.WorkerFinishedEvent;
import com.x.scrape.scraping.worker.event.WorkerStartedEvent;
import org.apache.logging.log4j.LogManager;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.InputStream;
import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static com.x.scrape.logging.ContextKeys.JOB_ID;
import static com.x.scrape.logging.ContextKeys.TASK_ID;
import static org.springframework.beans.factory.config.ConfigurableBeanFactory.SCOPE_PROTOTYPE;

@Component
@Scope(SCOPE_PROTOTYPE)
public class Worker {
	
	private final ContextLogger logger = new ContextLogger(LogManager.getLogger());
	
	private final JobTaskQueue jobTaskQueue;
	private final ScrapingService scrapingService;
	private final HttpService httpService;
	private final ApplicationEventPublisher applicationEventPublisher;
	
	private UUID jobId;
	private Instant startTime;
	
	public Worker(final JobTaskQueue jobTaskQueue,
	              final ScrapingService scrapingService,
	              final HttpService httpService,
	              final ApplicationEventPublisher applicationEventPublisher) {
		this.jobTaskQueue = jobTaskQueue;
		this.scrapingService = scrapingService;
		this.httpService = httpService;
		this.applicationEventPublisher = applicationEventPublisher;
	}
	
	/**
	 * Initializes the worker and configures it as a worker for a specific {@link Job}.
	 *
	 * @param jobId the id of the {@link Job}.
	 */
	public void init(final UUID jobId) {
		this.jobId = jobId;
	}
	
	public void start() {
		try (final CloseableContext ignored = logger.with(JOB_ID, jobId.toString())) {
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
			
			final ScrapingResult scrapingResult = scrapingService.scrape(
					task.getUrl(),
					task.getScrapingConfiguration()
			);
			downloadImages(task, scrapingResult.getResult());
			publishScrapingResultEvents(task, scrapingResult);
			
			logger.info("Task completed");
		} catch (final Exception e) {
			logger.error("Task execution failed.", e);
			applicationEventPublisher.publishEvent(new TaskFailedEvent(jobId, task.getId()));
		}
	}
	
	private void createTaskResultFolder(final Task task) {
		final File file = new File(task.getJob().getJobTaskResultsFolder() + "/" + task.getId());
		file.mkdirs();
	}
	
	private void publishScrapingResultEvents(final Task task,
	                                         final ScrapingResult scrapingResult) {
		applicationEventPublisher.publishEvent(
				TaskResultEvent.of(
						task,
						StorageHint.of(
								UUID.randomUUID() + ".json",
								task.getJob().getJobTaskResultsFolder() + "/" + task.getId() + "/"
						),
						new JsonPayload(scrapingResult.getResult())
				)
		);
		
		applicationEventPublisher.publishEvent(
				TaskResultEvent.of(
						task,
						StorageHint.of(
								"source.html",
								task.getJob().getJobTaskResultsFolder() + "/" + task.getId() + "/"
						),
						new StringPayload(scrapingResult.getRawPage())
				)
		);
	}
	
	// TODO offer this as a task too so that rate limiting can be applied properly in the future
	// Or make this use the same rate limiter in the code, Resilience4J will be used for this.
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
	 * @param task              task.
	 * @param imageDownloadTask image download sub-task.
	 * @param fileName          name to save the image under.
	 */
	private void downloadImage(final Task task,
	                           final ImageDownloadTask imageDownloadTask,
	                           final String fileName) {
		logger.info("Downloading image");
		final InputStream imageInputStream = httpService.get(imageDownloadTask.getUrl());
		
		applicationEventPublisher.publishEvent(
				TaskResultEvent.of(
						task,
						StorageHint.of(
								fileName,
								task.getJob().getJobTaskResultsFolder() + "/" + task.getId() + "/images/"
						),
						new ImagePayload(imageInputStream)
				)
		);
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
