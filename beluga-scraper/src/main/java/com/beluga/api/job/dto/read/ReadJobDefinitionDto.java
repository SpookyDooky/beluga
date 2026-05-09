package com.x.scrape.api.job.dto.read;

public class ReadJobDefinitionDto {
	
	private Long id;
	private ReadStorageConfigurationDto storage;
	private ReadExecutionConfigurationDto execution;
	private ReadScrapingConfigurationDto scraping;
	
	public Long getId() {
		return id;
	}
	
	public void setId(final Long id) {
		this.id = id;
	}
	
	public ReadStorageConfigurationDto getStorage() {
		return storage;
	}
	
	public void setStorage(final ReadStorageConfigurationDto storage) {
		this.storage = storage;
	}
	
	public ReadExecutionConfigurationDto getExecution() {
		return execution;
	}
	
	public void setExecution(final ReadExecutionConfigurationDto execution) {
		this.execution = execution;
	}
	
	public ReadScrapingConfigurationDto getScraping() {
		return scraping;
	}
	
	public void setScraping(final ReadScrapingConfigurationDto scraping) {
		this.scraping = scraping;
	}
}
