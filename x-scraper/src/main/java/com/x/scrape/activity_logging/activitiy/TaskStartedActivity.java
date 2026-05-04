package com.x.scrape.activity_logging.activitiy;

import java.net.URL;

import static com.x.scrape.activity_logging.activitiy.ActivityType.TASK_STARTED;

public class TaskStartedActivity extends Activity{
	
	private final URL url;
	
	public TaskStartedActivity(final URL url) {
		super(TASK_STARTED);
		this.url = url;
	}
	
	public URL getUrl() {
		return url;
	}
}
