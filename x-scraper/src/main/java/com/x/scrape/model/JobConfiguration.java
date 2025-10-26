package com.x.scrape.model;

import com.x.scrape.model.job.JobDefinition;
import com.x.scrape.model.job.UrlConfiguration;
import com.x.scrape.model.job.execution.ExecutionConfiguration;
import com.x.scrape.model.job.scraping_configuration.ScrapingConfiguration;
import com.x.scrape.model.job.storage.StorageConfiguration;
import com.x.scrape.persistence.shared.model.HasId;

public class JobConfiguration implements HasId {
	
	private Long id;
	private String name;
	
	private UrlConfiguration urlConfiguration;
	private ScrapingConfiguration scrapingConfiguration;
	private StorageConfiguration storageConfiguration;
	
	private ExecutionConfiguration executionConfiguration;
	
	@Override
	public Long getId() {
		return id;
	}
	
	@Override
	public void setId(final Long id) {
		this.id = id;
	}
	
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
	
	public ExecutionConfiguration getExecutionConfiguration() {
		return executionConfiguration;
	}
	
	public void setExecutionConfiguration(final ExecutionConfiguration executionConfiguration) {
		this.executionConfiguration = executionConfiguration;
	}
	
	public JobDefinition getJob() {
		return new JobDefinition(this);
	}
}
