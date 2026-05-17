package com.beluga.api.job.dto.read;

public class ReadJobDefinitionDto {
	
	private Long id;
	private ReadExecutionConfigurationDto execution;
	private ReadScrapingConfigurationDto scraping;
	
	public Long getId() {
		return id;
	}
	
	public void setId(final Long id) {
		this.id = id;
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
