package com.x.scrape.properties.scraping;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public class ScrapingProperties {
	
	@NotNull
	private String elementSelector;
	@NotEmpty
	private List<DataPointProperties> dataPoints;
	
	public String getElementSelector() {
		return elementSelector;
	}
	
	public void setElementSelector(final String elementSelector) {
		this.elementSelector = elementSelector;
	}
	
	public List<DataPointProperties> getDataPoints() {
		return dataPoints;
	}
	
	public void setDataPoints(final List<DataPointProperties> dataPoints) {
		this.dataPoints = dataPoints;
	}
}
