package com.beluga.execution.service.job;

import com.beluga.execution.event.job.JobFinishedEvent;
import com.beluga.execution.event.job.JobStartedEvent;
import com.beluga.execution.event.task.TaskCompletedEvent;
import com.beluga.execution.event.task.TaskFailedEvent;
import com.beluga.execution.model.job.Job;
import com.beluga.execution.model.task.Task;
import com.beluga.execution.service.task.JobTaskQueue;
import com.beluga.execution.service.worker.Worker;
import com.beluga.execution.service.worker.rate_limiting.JitterRateLimiter;
import com.beluga.logging.ContextLogger;
import com.beluga.model.job_definition.JobDefinition;
import com.beluga.service.task.TaskExecutionService;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import static com.beluga.execution.model.task.TaskStatus.PAUSED;
import static com.beluga.execution.model.task.TaskStatus.STOPPED;

/**
 * This service takes care of starting the correct amount of {@link Worker}'s for each {@link JobDefinition}.
 * Furthermore, for each {@link JobDefinition} it also places all tasks in the {@link JobTaskQueue}.
 */
@Service
public class JobExecutionService {
	
	private final ContextLogger logger;
	private final JobTaskQueue jobTaskQueue;
	private final ApplicationEventPublisher eventPublisher;
	private final Worker worker;
	private final TaskExecutionService taskExecutionService;

	private final Map<Long, Job> jobIdJobMap = new HashMap<>();
	private final Map<Long, AtomicInteger> jobActiveTasks = new HashMap<>();

	public JobExecutionService(final ContextLogger logger,
	                           final JobTaskQueue jobTaskQueue,
	                           final ApplicationEventPublisher eventPublisher,
	                           final Worker worker,
	                           final TaskExecutionService taskExecutionService) {
		this.logger = logger;
		this.jobTaskQueue = jobTaskQueue;
		this.eventPublisher = eventPublisher;
		this.worker = worker;
		this.taskExecutionService = taskExecutionService;
	}

	@Async
	public void execute(final Job job) {
		logger.info("Executing job");
		
		job.getTasks().forEach(jobTaskQueue::offerTask);
		job.getTasks().clear();

		jobIdJobMap.put(job.getId(), job);
		jobActiveTasks.put(job.getId(), new AtomicInteger(0));

		eventPublisher.publishEvent(new JobStartedEvent(job.getJobDefinitionId(), job.getId()));

		final JitterRateLimiter jitterRateLimiter = new JitterRateLimiter(job.getExecutionConfiguration().getTasksPerSecond());
		execute(job.getId(), jitterRateLimiter);
	}

	private void execute(final Long jobId,
						 final JitterRateLimiter rateLimiter) {
		while (!jobTaskQueue.isQueueEmpty(jobId)) {
			rateLimiter.acquire();

			jobTaskQueue.pollTask(jobId)
					.ifPresent(worker::execute);
			jobActiveTasks.get(jobId).incrementAndGet();
		}

		waitForAllTasksToFinish(jobId);
	}

	private void waitForAllTasksToFinish(final Long jobId) {
		while (jobActiveTasks.get(jobId).get() != 0) {
            try {
                Thread.sleep(100);
            } catch (final InterruptedException e) {
                throw new IllegalStateException(e);
            }
        }

		eventPublisher.publishEvent(new JobFinishedEvent(
				jobIdJobMap.get(jobId).getJobDefinitionId(),
				jobId
		));

		jobIdJobMap.remove(jobId);
		jobActiveTasks.remove(jobId);
	}

	@Async
	@EventListener
	public void onTaskCompletedEvent(final TaskCompletedEvent event) {
		jobActiveTasks.get(event.getJobId()).decrementAndGet();
	}

	@Async
	@EventListener
	public void onTaskFailedEvent(final TaskFailedEvent event) {
		jobActiveTasks.get(event.getJobId()).decrementAndGet();
	}
	
	/**
	 * Stops a running {@link Job}, and takes care of changing the status of all remaining tasks.
	 *
	 * @param jobId the id of the {@link Job}.
	 */
	public void stop(final Long jobId) {
		jobTaskQueue.clearTasks(jobId).stream()
				.map(Task::getId)
				.forEach(taskId -> taskExecutionService.setStatusById(taskId, STOPPED));
	}
	
	/**
	 * Pauses a running {@link Job}, and takes care of pausing all the remaining tasks.
	 *
	 * @param jobId the id of the {@link Job}.
	 */
	public void pause(final Long jobId) {
		jobTaskQueue.clearTasks(jobId).stream()
				.map(Task::getId)
				.forEach(taskId -> taskExecutionService.setStatusById(taskId, PAUSED));
	}
}
