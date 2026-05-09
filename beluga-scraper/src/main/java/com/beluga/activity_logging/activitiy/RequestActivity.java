package com.beluga.activity_logging.activitiy;

import java.net.URL;

import static com.beluga.activity_logging.activitiy.ActivityType.REQUEST;

/**
 * Represents the activity of requesting a page.
 */
public class RequestActivity extends Activity {
	
	private final URL url;
	private final Long duration;
	private final boolean success;
	
	public RequestActivity(final URL url,
	                       final Long duration,
	                       final boolean success) {
		super(REQUEST);
		this.url = url;
		this.duration = duration;
		this.success = success;
	}
	
	public URL getUrl() {
		return url;
	}
	
	public Long getDuration() {
		return duration;
	}
	
	public boolean isSuccess() {
		return success;
	}
}
