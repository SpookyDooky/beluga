package com.x.scrape.activity_logging.service;

import com.x.scrape.activity_logging.event.ActivityEvent;
import com.x.scrape.activity_logging.model.Activity;
import com.x.scrape.logging.ContextLogger;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicReference;

@Service
public class ActivityLoggingService {
	
	/**
	 * Interval in which logs should be flushed.
	 */
	private static final Long FLUSH_INTERVAL_MS = 5_000L;
	
	private final ContextLogger logger;
	private final ApplicationEventPublisher applicationEventPublisher;
	
	private final Map<UUID, AtomicReference<ConcurrentLinkedQueue<Activity>>> jobExecutionActivities = new ConcurrentHashMap<>();
	
	public ActivityLoggingService(final ContextLogger logger,
	                              final ApplicationEventPublisher applicationEventPublisher) {
		this.logger = logger;
		this.applicationEventPublisher = applicationEventPublisher;
	}
	
	@Async
	@EventListener
	public void onActivityEvent(final ActivityEvent event) {
		logger.info("Received new activity.");
		
		jobExecutionActivities.compute(
				event.getActivity().getContext().getJobId(),
				(key, value) -> {
					if (value == null) {
						value = new AtomicReference<>();
						value.set(new ConcurrentLinkedQueue<>());
					}
					
					final ConcurrentLinkedQueue<Activity> activityQueue = value.get();
					activityQueue.offer(event.getActivity());
					
					return value;
				}
		);
	}
	
	@Scheduled(fixedRate = 5_000)
	public void flushActivityLogs() {
		logger.info("Flushing activity logs");
		jobExecutionActivities.forEach(this::flushActivityLog);
	}
	
	private void flushActivityLog(final UUID jobId,
	                              final AtomicReference<ConcurrentLinkedQueue<Activity>> jobActivities) {
		
	}
}
