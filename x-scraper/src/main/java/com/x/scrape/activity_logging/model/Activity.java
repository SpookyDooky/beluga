package com.x.scrape.activity_logging.model;

import org.slf4j.MDC;

import java.time.Instant;

public abstract class Activity {
	
	/**
	 * Time of activity.
	 */
	private final Instant timestamp = Instant.now();
	
	private final ActivityType type;
	private final ActivityContext context;
	
	/**
	 * Creates an activity, and adds the context from the {@link MDC}.
	 *
	 * @param type the type of activity.
	 */
	public Activity(final ActivityType type) {
		this.type = type;
		context = new ActivityContext(MDC.getCopyOfContextMap());
	}
	
	public Instant getTimestamp() {
		return timestamp;
	}
	
	public ActivityType getType() {
		return type;
	}
	
	public ActivityContext getContext() {
		return context;
	}
}
