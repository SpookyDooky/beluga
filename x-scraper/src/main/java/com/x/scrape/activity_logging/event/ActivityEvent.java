package com.x.scrape.activity_logging.event;

import com.x.scrape.activity_logging.model.Activity;
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
