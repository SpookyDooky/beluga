package com.beluga.api.job.dto.base;

import jakarta.validation.constraints.NotEmpty;

public abstract class BaseScrapingConfigurationDto {
	
	@NotEmpty
	private String itemSelector;
	
	public String getItemSelector() {
		return itemSelector;
	}
	
	public void setItemSelector(final String itemSelector) {
		this.itemSelector = itemSelector;
	}
}
