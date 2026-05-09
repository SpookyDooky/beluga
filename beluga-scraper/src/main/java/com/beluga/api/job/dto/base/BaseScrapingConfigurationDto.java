package com.x.scrape.api.job.dto.base;

import jakarta.validation.constraints.NotEmpty;

public abstract class BaseScrapingConfigurationDto {
	
	@NotEmpty
	private String elementSelector;
	
	public String getElementSelector() {
		return elementSelector;
	}
	
	public void setElementSelector(final String elementSelector) {
		this.elementSelector = elementSelector;
	}
}
