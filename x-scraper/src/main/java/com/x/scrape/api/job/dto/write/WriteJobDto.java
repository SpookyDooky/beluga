package com.x.scrape.api.job.dto.write;

import com.x.scrape.api.job.dto.base.BaseJobDto;
import jakarta.validation.constraints.NotNull;

public class WriteJobDto extends BaseJobDto {
	
	@NotNull
	private WriteStorageConfigurationDto resultStorage;
	@NotNull
	private WriteExecutionConfigurationDto execution;
	@NotNull
	private WriteScrapingConfigurationDto scraping;
	
	public WriteStorageConfigurationDto getResultStorage() {
		return resultStorage;
	}
	
	public void setResultStorage(final WriteStorageConfigurationDto resultStorage) {
		this.resultStorage = resultStorage;
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
