package com.x.scrape.model.task;

import com.x.scrape.model.job.scraping_configuration.ScrapingConfiguration;

import java.net.URL;
import java.util.UUID;

public class Task {
	
	private final UUID id;
	private UUID jobId;
	
	private URL url;
	private ScrapingConfiguration scrapingConfiguration;
	
	public Task() {
		id = UUID.randomUUID();
	}
	
	public UUID getId() {
		return id;
	}
	
	public UUID getJobId() {
		return jobId;
	}
	
	public void setJobId(final UUID jobId) {
		this.jobId = jobId;
	}
	
	public URL getUrl() {
		return url;
	}
	
	public void setUrl(final URL url) {
		this.url = url;
	}
	
	public ScrapingConfiguration getScrapingConfiguration() {
		return scrapingConfiguration;
	}
	
	public void setScrapingConfiguration(final ScrapingConfiguration scrapingConfiguration) {
		this.scrapingConfiguration = scrapingConfiguration;
	}
}
