package com.x.scrape.properties.scraping;

import com.x.scrape.properties.scraping.storage.StorageProperties;
import com.x.scrape.properties.scraping.url.UrlProperties;

public class JobProperties {
	
	private String name;
	private UrlProperties url;
	
	private ScrapingProperties scraping;
	private StorageProperties storage;
	
	private int workers = 1;
	
	public String getName() {
		return name;
	}
	
	public void setName(final String name) {
		this.name = name;
	}
	
	public UrlProperties getUrl() {
		return url;
	}
	
	public void setUrl(final UrlProperties url) {
		this.url = url;
	}
	
	public ScrapingProperties getScraping() {
		return scraping;
	}
	
	public void setScraping(final ScrapingProperties scraping) {
		this.scraping = scraping;
	}
	
	public StorageProperties getStorage() {
		return storage;
	}
	
	public void setStorage(final StorageProperties storage) {
		this.storage = storage;
	}
	
	public int getWorkers() {
		return workers;
	}
	
	public void setWorkers(final int workers) {
		this.workers = workers;
	}
}
