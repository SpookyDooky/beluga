package com.beluga.execution.model.task;

import java.util.ArrayList;
import java.util.List;

public class ScrapingConfiguration {

	private Long id;
	private String elementSelector;
	private final List<DataPointConfiguration> dataPointConfigurations = new ArrayList<>();
	
	public Long getId() {
		return id;
	}
	
	public void setId(final Long id) {
		this.id = id;
	}
	
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
		this.dataPointConfigurations.clear();
		this.dataPointConfigurations.addAll(dataPointConfigurations);
	}
}
