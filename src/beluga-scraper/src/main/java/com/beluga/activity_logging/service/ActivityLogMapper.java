package com.beluga.activity_logging.service;

import com.beluga.activity_logging.activitiy.Activity;
import com.beluga.activity_logging.model.ActivityContext;
import com.beluga.activity_logging.model.ActivityLog;
import org.springframework.stereotype.Component;
import tools.jackson.databind.ObjectMapper;

@Component
public class ActivityLogMapper {
	
	private final ObjectMapper objectMapper;
	
	public ActivityLogMapper(final ObjectMapper objectMapper) {
		this.objectMapper = objectMapper;
	}
	
	public ActivityLog map(final Activity activity) {
		final ActivityLog activityLog = createActivityLog(activity);
		
		activityLog.setContext(mapContext(activity));
		
		return activityLog;
	}
	
	private ActivityLog createActivityLog(final Activity activity) {
		final ActivityLog activityLog = new ActivityLog();
		
		activityLog.setTimestamp(activity.getTimestamp());
		activityLog.setType(activity.getType());
		
		return activityLog;
	}
	
	private ActivityContext mapContext(final Activity activity) {
		final ActivityContext context = objectMapper.convertValue(activity, ActivityContext.class);
		
		context.remove("timestamp");
		context.remove("type");
		
		return context;
	}
}
