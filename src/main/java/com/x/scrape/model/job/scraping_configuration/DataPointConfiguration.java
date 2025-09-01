package com.x.scrape.model.job.scraping_configuration;

import com.x.scrape.model.types.ValueSelector;

public class DataPointConfiguration {
	
	private String selector;
	private String propertyName;
	
	private ValueSelector valueSelector;
	
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
	
	public ValueSelector getValueSelector() {
		return valueSelector;
	}
	
	public void setValueSelector(final ValueSelector valueSelector) {
		this.valueSelector = valueSelector;
	}
}
