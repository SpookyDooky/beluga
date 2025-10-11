package com.x.scrape.activity_logging.service;

import com.x.scrape.activity_logging.event.ActivityEvent;
import com.x.scrape.activity_logging.event.ActivityFlushEvent;
import com.x.scrape.activity_logging.model.Activity;
import com.x.scrape.logging.ContextLogger;
import com.x.scrape.model.task.event.task_result.StorageHint;
import com.x.scrape.scraping.job.JobRegistry;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicReference;

/**
 * This service takes care of collecting all the {@link ActivityEvent}'s. It temporarily stores the {@link Activity}'s
 * in memory before flushing them after x amount of time.
 */
@Service
public class ActivityLoggingService {
	
	/**
	 * Custom data time formatter, this is so that the format does not violate and file name restrictions.
	 */
	private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd_hh-mm-ss");
	
	private final ContextLogger logger;
	private final ApplicationEventPublisher applicationEventPublisher;
	private final JobRegistry jobRegistry;
	
	private final Map<UUID, AtomicReference<ConcurrentLinkedQueue<Activity>>> jobExecutionActivities = new ConcurrentHashMap<>();
	
	public ActivityLoggingService(final ContextLogger logger,
	                              final ApplicationEventPublisher applicationEventPublisher,
	                              final JobRegistry jobRegistry) {
		this.logger = logger;
		this.applicationEventPublisher = applicationEventPublisher;
		this.jobRegistry = jobRegistry;
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
	
	/**
	 * Flushes all the stored activities by sending an event. This event should be picked up
	 * at least by a service that handles saving data to the file system.
	 */
	// TODO - Should be configurable through properties but should also have a "good" default value.
	@Scheduled(fixedRate = 15_000)
	public void flushActivityLogs() {
		logger.info("Flushing activity logs");
		jobExecutionActivities.forEach(this::flushActivityLog);
	}
	
	private void flushActivityLog(final UUID jobId,
	                              final AtomicReference<ConcurrentLinkedQueue<Activity>> jobActivities) {
		final ConcurrentLinkedQueue<Activity> activities = jobActivities.getAndSet(new ConcurrentLinkedQueue<>());
		
		if (!activities.isEmpty()) {
			applicationEventPublisher.publishEvent(
					ActivityFlushEvent.of(
							StorageHint.of(
									getFileName(),
									getFolder(activities.peek())
							),
							activities
					)
			);
		}
	}
	
	private String getFileName() {
		final Instant currentTime = Instant.now();
		final String formattedTime = DATE_TIME_FORMATTER
				.format(LocalDateTime.ofInstant(currentTime, ZoneId.systemDefault()));
		
		return formattedTime + ".json";
	}
	
	private String getFolder(final Activity activity) {
		final UUID jobId = activity.getContext().getJobId();
		
		return jobRegistry.get(jobId)
				.getJobFolder() + "/logs";
	}
}
