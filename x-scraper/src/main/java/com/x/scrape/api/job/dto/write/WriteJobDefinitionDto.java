package com.x.scrape.api.job.dto.write;

import com.x.scrape.api.job.dto.base.BaseJobDefinitionDto;
import jakarta.validation.constraints.NotNull;

public class WriteJobDefinitionDto extends BaseJobDefinitionDto {
	
	@NotNull
	private WriteStorageConfigurationDto storage;
	@NotNull
	private WriteExecutionConfigurationDto execution;
	@NotNull
	private WriteScrapingConfigurationDto scraping;
	
	public WriteStorageConfigurationDto getStorage() {
		return storage;
	}
	
	public void setStorage(final WriteStorageConfigurationDto storage) {
		this.storage = storage;
	}
	
	public WriteExecutionConfigurationDto getExecution() {
		return execution;
	}
	
	public void setExecution(final WriteExecutionConfigurationDto execution) {
		this.execution = execution;
	}
	
	public WriteScrapingConfigurationDto getScraping() {
		return scraping;
	}
	
	public void setScraping(final WriteScrapingConfigurationDto scraping) {
		this.scraping = scraping;
	}
}
