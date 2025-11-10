package com.x.scrape.api.job.dto.base;

import com.x.scrape.model.job_definition.configuration.scraping_configuration.DataPointType;
import jakarta.validation.constraints.NotEmpty;

import static com.x.scrape.model.job_definition.configuration.scraping_configuration.DataPointType.TEXT;

public abstract class BaseScrapingDataPointDto {
	
	@NotEmpty
	private String selector;
	@NotEmpty
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
