package com.beluga.activity_logging.activitiy;

import java.time.Instant;

public abstract class Activity {
	
	private final Instant timestamp = Instant.now();
	private final ActivityType type;
	
	public Activity(final ActivityType type) {
		this.type = type;
	}
	
	public Instant getTimestamp() {
		return timestamp;
	}
	
	public ActivityType getType() {
		return type;
	}
}
