package com.x.scrape.model.job;

import com.x.scrape.model.job.scraping_configuration.ScrapingConfiguration;
import com.x.scrape.model.job.storage.StorageConfiguration;

import java.util.UUID;

public class Job {
	
	private final UUID id;
	
	private UrlConfiguration urlConfiguration;
	private ScrapingConfiguration scrapingConfiguration;
	private StorageConfiguration storageConfiguration;
	
	public Job() {
		id = UUID.randomUUID();
	}
	
	public UUID getId() {
		return id;
	}
	
	public UrlConfiguration getUrlConfiguration() {
		return urlConfiguration;
	}
	
	public void setUrlConfiguration(final UrlConfiguration urlConfiguration) {
		this.urlConfiguration = urlConfiguration;
	}
	
	public ScrapingConfiguration getScrapingConfiguration() {
		return scrapingConfiguration;
	}
	
	public void setScrapingConfiguration(final ScrapingConfiguration scrapingConfiguration) {
		this.scrapingConfiguration = scrapingConfiguration;
	}
	
	public StorageConfiguration getStorageConfiguration() {
		return storageConfiguration;
	}
	
	public void setStorageConfiguration(final StorageConfiguration storageConfiguration) {
		this.storageConfiguration = storageConfiguration;
	}
}
