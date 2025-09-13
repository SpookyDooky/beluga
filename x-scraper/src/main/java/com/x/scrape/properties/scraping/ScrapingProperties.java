package com.x.scrape.properties.scraping;

import java.util.List;

public class ScrapingProperties {
	
	private String elementSelector;
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
