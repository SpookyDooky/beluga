package com.beluga.activity_logging.event;

import com.beluga.activity_logging.activitiy.Activity;
import org.springframework.context.ApplicationEvent;

/**
 * Event that is used to publish new activities that have happened in the scraping engine.
 */
public class ActivityEvent extends ApplicationEvent {
	
	private final Activity activity;
	
	public ActivityEvent(final Activity activity) {
		super(activity);
		this.activity = activity;
	}
	
	public Activity getActivity() {
		return activity;
	}
}
