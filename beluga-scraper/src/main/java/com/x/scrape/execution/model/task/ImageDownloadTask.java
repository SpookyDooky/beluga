package com.x.scrape.execution.model.task;

import java.net.URL;

public class ImageDownloadTask {
	
	public URL url;
	private String propertyName;
	
	public URL getUrl() {
		return url;
	}
	
	public void setUrl(final URL url) {
		this.url = url;
	}
	
	public String getPropertyName() {
		return propertyName;
	}
	
	public void setPropertyName(final String propertyName) {
		this.propertyName = propertyName;
	}
}
