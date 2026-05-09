package com.beluga.execution.model.task;

import com.beluga.model.job_definition.configuration.scraping_configuration.DataPointType;

import static com.beluga.model.job_definition.configuration.scraping_configuration.DataPointType.TEXT;

public class DataPointConfiguration {
	
	private String selector;
	private String propertyName;
	private String attribute;
	private DataPointType type = TEXT;
	
	public String getSelector() {
		return selector;
	}
	
	public void setSelector(final String selector) {
		this.selector = selector;
	}
	
	public String getPropertyName() {
		return propertyName;
	}
	
	public void setPropertyName(final String propertyName) {
		this.propertyName = propertyName;
	}
	
	public String getAttribute() {
		return attribute;
	}
	
	public void setAttribute(final String attribute) {
		this.attribute = attribute;
	}
	
	public DataPointType getType() {
		return type;
	}
	
	public void setType(final DataPointType type) {
		this.type = type;
	}
}
