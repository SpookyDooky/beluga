package com.beluga.execution.service.worker;

import com.beluga.activity_logging.activitiy.TaskCompletedActivity;
import com.beluga.activity_logging.activitiy.TaskStartedActivity;
import com.beluga.activity_logging.event.ActivityEvent;
import com.beluga.execution.service.task.JobTaskQueue;
import com.beluga.execution.service.worker.event.WorkerFinishedEvent;
import com.beluga.execution.service.worker.event.WorkerStartedEvent;
import com.beluga.execution.service.worker.rate_limiting.JitterRateLimiter;
import com.beluga.http.HttpService;
import com.beluga.logging.CloseableContext;
import com.beluga.logging.ContextLogger;
import com.beluga.model.event.storable.payload.ImagePayload;
import com.beluga.model.event.storable.payload.JsonPayload;
import com.beluga.model.event.storable.payload.StringPayload;
import com.beluga.model.job_definition.JobDefinition;
import com.beluga.execution.model.task.ImageDownloadTask;
import com.beluga.execution.model.task.Task;
import com.beluga.execution.event.task.TaskCompletedEvent;
import com.beluga.execution.event.task.TaskFailedEvent;
import com.beluga.execution.event.task.TaskStartedEvent;
import com.beluga.execution.event.task.task_result.StorageHint;
import com.beluga.execution.event.task.task_result.TaskResultEvent;
import com.beluga.scraping.ScrapingService;
import com.beluga.scraping.model.ScrapingResult;
import com.beluga.util.TimingService;
import org.apache.logging.log4j.LogManager;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.io.InputStream;
import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static com.beluga.logging.ContextKeys.JOB_EXECUTION_ID;
import static com.beluga.logging.ContextKeys.WORKER_ID;
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
	
	private JitterRateLimiter rateLimiter;
	
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
	                 final JitterRateLimiter rateLimiter) {
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
			applicationEventPublisher.publishEvent(new ActivityEvent(new TaskStartedActivity(task.getUrl())));
			timingService.start(workerId);
			
			logger.info("Executing task.");
			
			final ScrapingResult scrapingResult = scrapingService.scrape(
					task.getUrl(),
					task.getScrapingConfiguration()
			);
			
			downloadImages(task, scrapingResult.getResult());
			publishScrapingResultEvents(task, scrapingResult);
			
			logger.info("Task completed");
			applicationEventPublisher.publishEvent(new ActivityEvent(new TaskCompletedActivity(task.getUrl(), timingService.stop(workerId))));
			applicationEventPublisher.publishEvent(new TaskCompletedEvent(task, task.getJob().getJobTaskResultsFolder() + "/" + task.getId() + "/"));
		} catch (final Exception e) {
			logger.error("Task execution failed.", e);
			applicationEventPublisher.publishEvent(new TaskFailedEvent(task));
			timingService.stop(workerId);
		}
	}
	
	private void downloadImages(final Task task,
	                            final List<Map<String, Object>> scrapedData) {
		scrapedData.forEach(elementScrapedData -> {
			final List<ImageDownloadTask> imageDownloadTasks = task.getImageDownloadsTask(elementScrapedData);
			
			for (final ImageDownloadTask imageDownloadTask : imageDownloadTasks) {
				final String fileName = UUID.randomUUID() + ".png";
				downloadImage(task, imageDownloadTask, fileName);
				
				addImagePathToResult(elementScrapedData, fileName, imageDownloadTask.getPropertyName());
			}
		});
	}
	
	private void publishScrapingResultEvents(final Task task,
	                                         final ScrapingResult scrapingResult) {
		applicationEventPublisher.publishEvent(
				TaskResultEvent.of(
						task,
						StorageHint.of(
								"data.json",
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
								task.getJob().getJobTaskResultsFolder() + "/" + task.getId() + "/files/"
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
								task.getJob().getJobTaskResultsFolder() + "/" + task.getId() + "/files/"
						),
						new ImagePayload(imageInputStream)
				)
		);
	}
	
	private void addImagePathToResult(final Map<String, Object> scrapedData,
	                                  final String fileName,
	                                  final String propertyName) {
		final String urlValue = (String) scrapedData.get(propertyName);
		scrapedData.remove(propertyName);
		
		scrapedData.put(propertyName + "." + propertyName, urlValue);
		scrapedData.put(propertyName + ".fileName", fileName);
	}
	
	private void finish() {
		final long totalTime = Instant.now().toEpochMilli() - startTime.toEpochMilli();
		logger.info("Worker finished in: " + totalTime + "ms");
		
		applicationEventPublisher.publishEvent(new WorkerFinishedEvent(jobId));
	}
}
