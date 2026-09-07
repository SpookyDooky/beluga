package com.beluga.execution.service.worker;

import com.beluga.activity_logging.activitiy.TaskCompletedActivity;
import com.beluga.activity_logging.activitiy.TaskStartedActivity;
import com.beluga.activity_logging.event.ActivityEvent;
import com.beluga.execution.event.task.TaskCompletedEvent;
import com.beluga.execution.event.task.TaskFailedEvent;
import com.beluga.execution.event.task.TaskStartedEvent;
import com.beluga.execution.event.task.task_result.TaskResultEvent;
import com.beluga.execution.model.job.Job;
import com.beluga.execution.model.task.Task;
import com.beluga.http.HttpService;
import com.beluga.logging.CloseableContext;
import com.beluga.logging.ContextLogger;
import com.beluga.model.event.storable.payload.JsonPayload;
import com.beluga.model.event.storable.payload.StringPayload;
import com.beluga.scraping.ScrapingService;
import com.beluga.scraping.model.ScrapingResult;
import com.beluga.util.TimingService;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.UUID;

import static com.beluga.logging.ContextKeys.JOB_EXECUTION_ID;
import static com.beluga.logging.ContextKeys.WORKER_ID;
import static org.springframework.beans.factory.config.ConfigurableBeanFactory.SCOPE_PROTOTYPE;

@Component
@Scope(SCOPE_PROTOTYPE)
public class Worker {

    private final UUID workerId = UUID.randomUUID();

    private final ContextLogger logger;
    private final ScrapingService scrapingService;
    private final HttpService httpService;
    private final ApplicationEventPublisher applicationEventPublisher;
    private final TimingService timingService;

    private Long jobId;
    private WorkerTaskCompletedCallback completedCallback;

    public Worker(final ContextLogger logger,
                  final ScrapingService scrapingService,
                  final HttpService httpService,
                  final ApplicationEventPublisher applicationEventPublisher,
                  final TimingService timingService) {
        this.logger = logger;
        this.scrapingService = scrapingService;
        this.httpService = httpService;
        this.applicationEventPublisher = applicationEventPublisher;
        this.timingService = timingService;
    }

    public UUID getWorkerId() {
        return workerId;
    }

    /**
     * Initializes the worker.
     *
     * @param jobId             The id of the {@link Job} this worker is for.
     * @param completedCallback callback to use when the {@link Worker} completes a {@link Task}.
     */
    public void init(final Long jobId,
                     final WorkerTaskCompletedCallback completedCallback) {
        this.jobId = jobId;
        this.completedCallback = completedCallback;
    }

    /**
     * Executes a {@link Task}.
     *
     * @param task the {@link Task} to execute.
     */
    public void execute(final Task task) {
        try (final CloseableContext ignored = logger.with(
                JOB_EXECUTION_ID, jobId.toString(),
                WORKER_ID, workerId.toString()
        )) {
            executeTask(task);
            completedCallback.complete(this);
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

            publishScrapingResultEvents(task, scrapingResult);

            logger.info("Task completed");
            applicationEventPublisher.publishEvent(new ActivityEvent(new TaskCompletedActivity(task.getUrl(), timingService.stop(workerId))));
            applicationEventPublisher.publishEvent(new TaskCompletedEvent(task));
        } catch (final Exception e) {
            logger.error("Task execution failed.", e);
            applicationEventPublisher.publishEvent(new TaskFailedEvent(task));
            timingService.stop(workerId);
        }
    }


    private void publishScrapingResultEvents(final Task task,
                                             final ScrapingResult scrapingResult) {
        applicationEventPublisher.publishEvent(TaskResultEvent.of(task, "data.json", new JsonPayload(scrapingResult.getResult())));
        applicationEventPublisher.publishEvent(TaskResultEvent.of(task, "source.html", new StringPayload(scrapingResult.getRawPage())));
    }
}
