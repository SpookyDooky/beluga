package com.x.scrape.activity_logging.service;

import com.x.scrape.activity_logging.event.ActivityEvent;
import com.x.scrape.activity_logging.model.Activity;
import com.x.scrape.logging.ContextLogger;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

@Service
public class ActivityLoggingService {
	
	/**
	 * Interval in which logs should be flushed.
	 */
	private static final Long FLUSH_INTERVAL_MS = 5_000L;
	
	private final ContextLogger logger;
	
	private final Map<UUID, ConcurrentLinkedQueue<Activity>> jobExecutionActivities = new ConcurrentHashMap<>();
	
	public ActivityLoggingService(final ContextLogger logger) {
		this.logger = logger;
	}
	
	@Async
	@EventListener
	public void onActivityEvent(final ActivityEvent activityEvent) {
		logger.info("Received new activity.");
	}
}
