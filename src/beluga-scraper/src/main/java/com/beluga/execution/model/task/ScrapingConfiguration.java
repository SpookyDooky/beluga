package com.beluga.execution.model.task;

import com.beluga.execution.model.task.extraction_configuration.ExtractionConfiguration;

import java.util.ArrayList;
import java.util.List;

public class ScrapingConfiguration {

	private Long id;
	private String itemSelector;
	private final List<ExtractionConfiguration> extractionConfigurations = new ArrayList<>();
	
	public Long getId() {
		return id;
	}
	
	public void setId(final Long id) {
		this.id = id;
	}
	
	public String getItemSelector() {
		return itemSelector;
	}
	
	public void setItemSelector(final String itemSelector) {
		this.itemSelector = itemSelector;
	}
	
	public List<ExtractionConfiguration> getExtractionConfigurations() {
		return extractionConfigurations;
	}
	
	public void setExtractionConfigurations(final List<ExtractionConfiguration> extractionConfigurations) {
		this.extractionConfigurations.clear();
		this.extractionConfigurations.addAll(extractionConfigurations);
	}
}
