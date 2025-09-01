package com.x.scrape.properties.scraping.url;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class UrlProperties {
	
	private final List<URL> urls = new ArrayList<>();
	
	private URL baseUrl;
	
	public List<URL> getUrls() {
		return urls;
	}
	
	public void setUrls(final List<URL> urls) {
		this.urls.clear();
		this.urls.addAll(urls);
	}
	
	public URL getBaseUrl() {
		return baseUrl;
	}
	
	public void setBaseUrl(final URL baseUrl) {
		this.baseUrl = baseUrl;
	}
}
