package com.beluga.properties.scraping;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;

import java.util.List;

public class ScrapingProperties {
	
	@NotBlank
	private String itemSelector;
	@NotEmpty
	private List<@Valid DataPointProperties> dataPoints;
	
	public String getItemSelector() {
		return itemSelector;
	}
	
	public void setItemSelector(final String itemSelector) {
		this.itemSelector = itemSelector;
	}
	
	public List<DataPointProperties> getDataPoints() {
		return dataPoints;
	}
	
	public void setDataPoints(final List<DataPointProperties> dataPoints) {
		this.dataPoints = dataPoints;
	}
}
