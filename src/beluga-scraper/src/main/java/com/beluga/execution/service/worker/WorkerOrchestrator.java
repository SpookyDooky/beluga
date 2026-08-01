package com.beluga.execution.service.worker;

import com.beluga.execution.model.task.Task;
import com.beluga.execution.service.task.JobTaskQueue;
import com.beluga.execution.service.worker.event.JobWorkersFinishedEvent;
import com.beluga.execution.service.worker.event.WorkerFinishedEvent;
import com.beluga.execution.service.worker.rate_limiting.JitterRateLimiter;
import com.beluga.logging.CloseableContext;
import com.beluga.logging.ContextLogger;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

import static com.beluga.logging.ContextKeys.JOB_ID;

@Component
public class WorkerOrchestrator {

	private final ContextLogger logger;
	private final ApplicationContext applicationContext;
	private final ApplicationEventPublisher applicationEventPublisher;
	private final JobTaskQueue jobTaskQueue;

	private final Map<Long, Integer> activeWorkersPerJob = new ConcurrentHashMap<>();

	private final Map<Long, List<Worker>> jobWorkers = new ConcurrentHashMap<>();
	private final Map<Long, Queue<Worker>> jobIdleWorkers = new ConcurrentHashMap<>();

	public WorkerOrchestrator(final ContextLogger logger,
							  final ApplicationContext applicationContext,
	                          final ApplicationEventPublisher applicationEventPublisher,
							  final JobTaskQueue jobTaskQueue) {
		this.logger = logger;
		this.applicationContext = applicationContext;
		this.applicationEventPublisher = applicationEventPublisher;
		this.jobTaskQueue = jobTaskQueue;
    }
	
	public void startWorkers(final Long jobId,
	                         final double rateLimit,
	                         final int workers) {
		try (final CloseableContext ignored = logger.with(JOB_ID, jobId.toString())) {
			registerWorkers(jobId, workers);
			jobIdleWorkers.put(jobId, new LinkedList<>(jobWorkers.get(jobId)));

			final JitterRateLimiter rateLimiter = new JitterRateLimiter(rateLimit);
			new Thread(() -> {
				manageWorkers(jobId, rateLimiter, workers);
			}).start();
		}
	}

	private void registerWorkers(final Long jobId,
								 final int workerCount) {
		logger.info("Registering workers");

		for (int i = 0; i < workerCount; i++) {
			final Worker worker = applicationContext.getBean(Worker.class);
			worker.init(jobId, completedCallback(jobId));

			jobWorkers.compute(jobId, (jobIdKey, workers) -> {
				if (workers == null) {
					workers = new ArrayList<>();
				}

				workers.add(worker);
				return workers;
			});
		}
	}

	private WorkerTaskCompletedCallback completedCallback(final Long jobId) {
		return worker -> {
			final Queue<Worker> idleWorkers = jobIdleWorkers.get(jobId);
			idleWorkers.offer(worker);
		};
	}

	private void manageWorkers(final Long jobId,
	                           final JitterRateLimiter rateLimiter,
							   final int workers) {
		while (!jobTaskQueue.isQueueEmpty(jobId)) {
			rateLimiter.acquire();

			final Task task = jobTaskQueue.pollTask(jobId)
					.orElseThrow();
			final Worker worker = getIdleWorker(jobId);
			worker.execute(task);
		}

		waitForWorkersToFinish(jobId, workers);
		// Destroy beans
	}

	private Worker getIdleWorker(final Long jobId) {
		final Queue<Worker> idleWorkers = jobIdleWorkers.get(jobId);

		while (idleWorkers.isEmpty()) {
            try {
                Thread.sleep(50);
            } catch (final InterruptedException e) {
                throw new RuntimeException(e);
            }
        }

		return idleWorkers.remove();
	}

	private void waitForWorkersToFinish(final Long jobId,
										final int workers) {
		final Queue<Worker> idleWorkers = jobIdleWorkers.get(jobId);

		while (idleWorkers.size() != workers) {
			try {
				Thread.sleep(50);
			} catch (final InterruptedException e) {
				throw new RuntimeException(e);
			}
		}
	}


	@EventListener
	public void onWorkerFinishedEvent(final WorkerFinishedEvent event) {
		final int activeWorkers = activeWorkersPerJob.get(event.getJobId()) - 1;
		activeWorkersPerJob.put(event.getJobId(), activeWorkers);
		
		if (activeWorkers <= 0) {
			applicationEventPublisher.publishEvent(new JobWorkersFinishedEvent(event.getJobId()));
		}
	}
}
