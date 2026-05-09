package com.beluga.properties.scraping;

import com.beluga.model.job_definition.configuration.scraping_configuration.DataPointType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import static com.beluga.model.job_definition.configuration.scraping_configuration.DataPointType.TEXT;

public class DataPointProperties {
	
	@NotBlank
	private String selector;
	@NotBlank
	private String propertyName;
	
	private String attribute;
	@NotNull
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
