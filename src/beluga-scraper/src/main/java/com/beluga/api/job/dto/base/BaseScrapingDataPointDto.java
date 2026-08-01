package com.beluga.api.job.dto.base;

import com.beluga.model.job_definition.configuration.scraping_configuration.DataPointType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;

import static com.beluga.model.job_definition.configuration.scraping_configuration.DataPointType.TEXT;

public abstract class BaseScrapingDataPointDto {
	
	@NotBlank
	private String selector;
	@NotEmpty
	private String field;
	private String attribute;
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
