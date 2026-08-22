package com.beluga.properties.scraping;

import jakarta.validation.constraints.NotBlank;

public class ScrapingProperties {
	
	@NotBlank
	private String itemSelector;
	
	public String getItemSelector() {
		return itemSelector;
	}
	
	public void setItemSelector(final String itemSelector) {
		this.itemSelector = itemSelector;
	}
}
