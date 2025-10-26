package com.x.scrape.activity_logging.model;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static com.x.scrape.logging.ContextKeys.*;

public class ActivityContext extends HashMap<String, String> {
	
	ActivityContext(final Map<String, String> context) {
		super(context);
	}
	
	public Long getJobId() {
		return Long.valueOf(get(JOB_EXECUTION_ID));
	}
	
	public UUID getTaskId() {
		return UUID.fromString(get(TASK_ID));
	}
}
