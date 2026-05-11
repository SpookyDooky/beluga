package com.beluga.properties.scraping;

import com.beluga.model.job_definition.configuration.scraping_configuration.DataPointType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import static com.beluga.model.job_definition.configuration.scraping_configuration.DataPointType.TEXT;

public class DataPointProperties {
	
	@NotBlank
	private String selector;
	@NotBlank
	private String field;
	
	private String attribute;
	@NotNull
	private DataPointType type = TEXT;
	
	public String getSelector() {
		return selector;
	}
	
	public void setSelector(final String selector) {
		this.selector = selector;
	}
	
	public String getField() {
		return field;
	}
	
	public void setField(final String field) {
		this.field = field;
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
