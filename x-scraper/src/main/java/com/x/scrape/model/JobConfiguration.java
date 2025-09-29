package com.x.scrape.model;

import com.x.scrape.model.job.Job;
import com.x.scrape.model.job.UrlConfiguration;
import com.x.scrape.model.job.scraping_configuration.ScrapingConfiguration;
import com.x.scrape.model.job.storage.StorageConfiguration;

public class JobConfiguration {
	
	private String name;
	
	private UrlConfiguration urlConfiguration;
	private ScrapingConfiguration scrapingConfiguration;
	private StorageConfiguration storageConfiguration;
	
	private int workers;
	
	public String getName() {
		return name;
	}
	
	public void setName(final String name) {
		this.name = name;
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
	
	public int getWorkers() {
		return workers;
	}
	
	public void setWorkers(final int workers) {
		this.workers = workers;
	}
	
	public Job getJob() {
		return new Job(this);
	}
}
