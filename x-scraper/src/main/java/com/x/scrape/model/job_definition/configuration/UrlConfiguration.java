package com.x.scrape.model.job_definition.configuration;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class UrlConfiguration {

	private List<URL> urls = new ArrayList<>();
	private String urlFile;
	
	public List<URL> getUrls() {
		return urls;
	}
	
	public void setUrls(final List<URL> urls) {
		this.urls = urls;
	}
	
	public String getUrlFile() {
		return urlFile;
	}
	
	public void setUrlFile(final String urlFile) {
		this.urlFile = urlFile;
	}
}
