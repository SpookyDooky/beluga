package com.x.scrape.activity_logging.model;

import java.net.URL;

import static com.x.scrape.activity_logging.model.ActivityType.REQUEST;

/**
 * Represents the activity of requesting a page.
 */
public class RequestActivity extends Activity {
	
	private final URL url;
	private final Long duration;
	
	public RequestActivity(final URL url,
	                       final Long duration) {
		super(REQUEST);
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
