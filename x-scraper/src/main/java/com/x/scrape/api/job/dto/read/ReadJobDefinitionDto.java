package com.x.scrape.api.job.dto.read;

public class ReadJobDefinitionDto {
	
	private Long id;
	private ReadStorageConfigurationDto resultStorage;
	private ReadExecutionConfigurationDto execution;
	private ReadScrapingConfigurationDto scraping;
	
	public Long getId() {
		return id;
	}
	
	public void setId(final Long id) {
		this.id = id;
	}
	
	public ReadStorageConfigurationDto getResultStorage() {
		return resultStorage;
	}
	
	public void setResultStorage(final ReadStorageConfigurationDto resultStorage) {
		this.resultStorage = resultStorage;
	}
	
	public ReadExecutionConfigurationDto getExecution() {
		return execution;
	}
	
	public void setExecution(final ReadExecutionConfigurationDto execution) {
		this.execution = execution;
	}
}
