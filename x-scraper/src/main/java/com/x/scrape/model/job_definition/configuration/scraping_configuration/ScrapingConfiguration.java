package com.x.scrape.model.job_definition.configuration.scraping_configuration;

import java.util.ArrayList;
import java.util.List;

public class ScrapingConfiguration {
	
	private String elementSelector;
	private List<DataPointConfiguration> dataPointConfigurations = new ArrayList<>();
	
	public String getElementSelector() {
		return elementSelector;
	}
	
	public void setElementSelector(final String elementSelector) {
		this.elementSelector = elementSelector;
	}
	
	public List<DataPointConfiguration> getDataPointConfigurations() {
		return dataPointConfigurations;
	}
	
	public void setDataPointConfigurations(final List<DataPointConfiguration> dataPointConfigurations) {
		this.dataPointConfigurations = dataPointConfigurations;
	}
}
