package com.x.scrape.activity_logging.model;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static com.x.scrape.logging.ContextKeys.JOB_ID;
import static com.x.scrape.logging.ContextKeys.TASK_ID;

public class ActivityContext extends HashMap<String, String> {
	
	ActivityContext(final Map<String, String> context) {
		super(context);
	}
	
	public UUID getJobId() {
		return UUID.fromString(get(JOB_ID));
	}
	
	public UUID getTaskId() {
		return UUID.fromString(get(TASK_ID));
	}
}
