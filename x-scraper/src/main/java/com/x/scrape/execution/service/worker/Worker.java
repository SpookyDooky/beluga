package com.x.scrape.execution.service.worker;

import com.google.common.util.concurrent.RateLimiter;
import com.x.scrape.activity_logging.event.ActivityEvent;
import com.x.scrape.activity_logging.model.TaskCompletedActivity;
import com.x.scrape.http.HttpService;
import com.x.scrape.logging.CloseableContext;
import com.x.scrape.logging.ContextLogger;
import com.x.scrape.model.event.storable.payload.ImagePayload;
import com.x.scrape.model.event.storable.payload.JsonPayload;
import com.x.scrape.model.event.storable.payload.StringPayload;
import com.x.scrape.model.job_definition.JobDefinition;
import com.x.scrape.model.task.ImageDownloadTask;
import com.x.scrape.model.task.Task;
import com.x.scrape.model.task.event.TaskCompletedEvent;
import com.x.scrape.model.task.event.TaskFailedEvent;
import com.x.scrape.model.task.event.TaskStartedEvent;
import com.x.scrape.model.task.event.task_result.StorageHint;
import com.x.scrape.model.task.event.task_result.TaskResultEvent;
import com.x.scrape.scraping.ScrapingService;
import com.x.scrape.scraping.model.ScrapingResult;
import com.x.scrape.execution.service.task.JobTaskQueue;
import com.x.scrape.execution.service.worker.event.WorkerFinishedEvent;
import com.x.scrape.execution.service.worker.event.WorkerStartedEvent;
import com.x.scrape.util.TimingService;
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

import static com.x.scrape.logging.ContextKeys.*;
import static org.springframework.beans.factory.config.ConfigurableBeanFactory.SCOPE_PROTOTYPE;

@Component
@Scope(SCOPE_PROTOTYPE)
public class Worker {
	
	private final ContextLogger logger = new ContextLogger(LogManager.getLogger());
	
	private final JobTaskQueue jobTaskQueue;
	private final ScrapingService scrapingService;
	private final HttpService httpService;
	private final ApplicationEventPublisher applicationEventPublisher;
	private final TimingService timingService;
	
	private Long jobId;
	private Instant startTime;
	private final UUID workerId = UUID.randomUUID();
	
	private RateLimiter rateLimiter;
	
	public Worker(final JobTaskQueue jobTaskQueue,
	              final ScrapingService scrapingService,
	              final HttpService httpService,
	              final ApplicationEventPublisher applicationEventPublisher,
	              final TimingService timingService) {
		this.jobTaskQueue = jobTaskQueue;
		this.scrapingService = scrapingService;
		this.httpService = httpService;
		this.applicationEventPublisher = applicationEventPublisher;
		this.timingService = timingService;
	}
	
	/**
	 * Initializes the worker and configures it as a worker for a specific {@link JobDefinition}.
	 *
	 * @param jobId the id of the {@link JobDefinition}.
	 */
	public void init(final Long jobId,
	                 final RateLimiter rateLimiter) {
		this.jobId = jobId;
		this.rateLimiter = rateLimiter;
	}
	
	public void start() {
		try (final CloseableContext context = logger.with(JOB_EXECUTION_ID, jobId.toString())) {
			context.put(WORKER_ID, workerId.toString());
			logger.info("Worker starting.");
			
			applicationEventPublisher.publishEvent(new WorkerStartedEvent(jobId));
			startTime = Instant.now();
			
			while (!jobTaskQueue.isQueueEmpty(jobId)) {
				rateLimiter.acquire();
				jobTaskQueue.pollTask(jobId)
						.ifPresent(this::executeTask);
			}
			
			finish();
		}
	}
	
	private void executeTask(final Task task) {
		try (final CloseableContext ignored = logger.with(task)) {
			applicationEventPublisher.publishEvent(new TaskStartedEvent(task));
			timingService.start(task.getId());
			
			logger.info("Executing task.");
			
			// Should not be the responsibility of the worker
			createTaskResultFolder(task);
			
			final ScrapingResult scrapingResult = scrapingService.scrape(
					task.getUrl(),
					task.getScrapingConfiguration()
			);
			
			downloadImages(task, scrapingResult.getResult());
			publishScrapingResultEvents(task, scrapingResult);
			
			logger.info("Task completed");
			applicationEventPublisher.publishEvent(new ActivityEvent(new TaskCompletedActivity(task.getUrl(), timingService.stop(task.getId()))));
			applicationEventPublisher.publishEvent(new TaskCompletedEvent(task));
		} catch (final Exception e) {
			logger.error("Task execution failed.", e);
			applicationEventPublisher.publishEvent(new TaskFailedEvent(task));
		}
	}
	
	private void createTaskResultFolder(final Task task) {
		final File file = new File(task.getJob().getJobTaskResultsFolder() + "/" + task.getId());
		file.mkdirs();
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
