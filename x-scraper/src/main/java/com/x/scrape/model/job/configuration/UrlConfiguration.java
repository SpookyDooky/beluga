package com.x.scrape.model.job.configuration;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class UrlConfiguration {

	private final List<URL> urls = new ArrayList<>();
	private String urlFile;
	
	public List<URL> getUrls() {
		return urls;
	}
	
	public String getUrlFile() {
		return urlFile;
	}
	
	public void setUrlFile(final String urlFile) {
		this.urlFile = urlFile;
	}
}
