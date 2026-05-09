package com.x.scrape.properties.scraping;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;

import java.util.List;

public class ScrapingProperties {
	
	@NotBlank
	private String elementSelector;
	@NotEmpty
	private List<@Valid DataPointProperties> dataPoints;
	
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
