package com.x.scrape.model.task;

import java.util.ArrayList;
import java.util.List;

public class TaskScrapingConfiguration {

	private Long id;
	private String elementSelector;
	private final List<TaskDataPointConfiguration> dataPointConfigurations = new ArrayList<>();
	
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
	
	public List<TaskDataPointConfiguration> getDataPointConfigurations() {
		return dataPointConfigurations;
	}
	
	public void setDataPointConfigurations(final List<TaskDataPointConfiguration> dataPointConfigurations) {
		this.dataPointConfigurations.clear();
		this.dataPointConfigurations.addAll(dataPointConfigurations);
	}
}
