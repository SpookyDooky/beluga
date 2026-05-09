package com.x.scrape.execution.service.worker;

import com.x.scrape.execution.service.worker.event.JobWorkersFinishedEvent;
import com.x.scrape.execution.service.worker.event.WorkerFinishedEvent;
import com.x.scrape.execution.service.worker.rate_limiting.JitterRateLimiter;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class WorkerOrchestrator {
	
	private final ApplicationContext applicationContext;
	private final ApplicationEventPublisher applicationEventPublisher;
	
	private final Map<Long, Integer> activeWorkersPerJob = new ConcurrentHashMap<>();
	
	public WorkerOrchestrator(final ApplicationContext applicationContext,
	                          final ApplicationEventPublisher applicationEventPublisher) {
		this.applicationContext = applicationContext;
		this.applicationEventPublisher = applicationEventPublisher;
	}
	
	public void startWorkers(final Long jobId,
	                         final double rateLimit,
	                         final int workers) {
		final JitterRateLimiter rateLimiter = new JitterRateLimiter(rateLimit);
		activeWorkersPerJob.put(jobId, 0);
		
		for (int i = 0; i < workers; i++) {
			final Worker worker = applicationContext.getBean(Worker.class);
			worker.init(jobId, rateLimiter);
			
			final int activeWorkers = activeWorkersPerJob.get(jobId);
			activeWorkersPerJob.put(jobId, activeWorkers + 1);
			
			new Thread(worker::start)
					.start();
		}
	}
	
	// Todo we should keep track of failed workers, because that means tasks failed these should be reran
	// To actually complete the job
	@EventListener
	public void onWorkerFinishedEvent(final WorkerFinishedEvent event) {
		final int activeWorkers = activeWorkersPerJob.get(event.getJobId()) - 1;
		activeWorkersPerJob.put(event.getJobId(), activeWorkers);
		
		if (activeWorkers <= 0) {
			applicationEventPublisher.publishEvent(new JobWorkersFinishedEvent(event.getJobId()));
		}
	}
}
