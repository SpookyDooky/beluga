package com.x.scrape.properties.scraping;

import com.x.scrape.model.ValueSelector;

import static com.x.scrape.model.ValueSelector.TEXT;

public class DataPointProperties {
	
	private String selector;
	private String propertyName;
	private ValueSelector valueSelector = TEXT;
	
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
