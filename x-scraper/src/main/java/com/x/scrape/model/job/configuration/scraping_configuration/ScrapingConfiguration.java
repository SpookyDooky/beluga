package com.x.scrape.model.job.configuration.scraping_configuration;

import java.util.ArrayList;
import java.util.List;

public class ScrapingConfiguration {
	
	private String elementSelector;
	private final List<DataPointConfiguration> dataPointConfigurations = new ArrayList<>();
	
	public String getElementSelector() {
		return elementSelector;
	}
	
	public void setElementSelector(final String elementSelector) {
		this.elementSelector = elementSelector;
	}
	
	public List<DataPointConfiguration> getDataPointConfigurations() {
		return dataPointConfigurations;
	}
}
