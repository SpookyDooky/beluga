package com.x.scrape.activity_logging.model;

import java.net.URL;

import static com.x.scrape.activity_logging.model.ActivityType.REQUEST;

/**
 * Represents the activity of requesting a page.
 */
public class RequestActivity extends Activity {
	
	private final URL url;
	
	public RequestActivity(final URL url) {
		super(REQUEST);
		this.url = url;
	}
	
	public URL getUrl() {
		return url;
	}
}
