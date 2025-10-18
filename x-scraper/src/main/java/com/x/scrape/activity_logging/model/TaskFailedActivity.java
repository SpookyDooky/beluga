package com.x.scrape.activity_logging.model;

import java.net.URL;

import static com.x.scrape.activity_logging.model.ActivityType.TASK_FAILED;

public class TaskFailedActivity extends Activity {
	
	private final URL url;
	private final Long duration;
	
	public TaskFailedActivity(final URL url,
	                          final Long duration) {
		super(TASK_FAILED);
		this.url = url;
		this.duration = duration;
	}
	
	public URL getUrl() {
		return url;
	}
	
	public Long getDuration() {
		return duration;
	}
	
}
