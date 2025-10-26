package com.x.scrape.execution.model;

import com.x.scrape.model.job_definition.JobDefinition;
import com.x.scrape.model.job_definition.JobExecution;
import com.x.scrape.model.job_definition.configuration.execution_configuration.ExecutionConfiguration;
import com.x.scrape.model.job_definition.configuration.scraping_configuration.ScrapingConfiguration;
import com.x.scrape.model.job_definition.configuration.storage_configuration.StorageConfiguration;

/**
 * This is aimed at the execution of a {@link JobDefinition}.
 */
public class Job {
	
	/**
	 * This is the id of the {@link JobExecution}.
	 */
	private Long id;
	
	private Long jobDefinitionId;
	private String jobName;
	
	private ScrapingConfiguration scrapingConfiguration;
	private StorageConfiguration storageConfiguration;
	private ExecutionConfiguration executionConfiguration;
	
	public Long getId() {
		return id;
	}
	
	public void setId(final Long id) {
		this.id = id;
	}
	
	public Long getJobDefinitionId() {
		return jobDefinitionId;
	}
	
	public void setJobDefinitionId(final Long jobDefinitionId) {
		this.jobDefinitionId = jobDefinitionId;
	}
	
	public String getJobName() {
		return jobName;
	}
	
	public void setJobName(final String jobName) {
		this.jobName = jobName;
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
}
