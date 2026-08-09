package com.beluga.execution.model.task;

import com.beluga.execution.model.task.extraction_configuration.ExtractionConfiguration;

public class DataPointConfiguration {
	
	private String selector;
	private ExtractionConfiguration extractionConfiguration;

	public String getSelector() {
		return selector;
	}

	public void setSelector(final String selector) {
		this.selector = selector;
	}

	public ExtractionConfiguration getExtractionConfiguration() {
		return extractionConfiguration;
	}

	public void setExtractionConfiguration(ExtractionConfiguration extractionConfiguration) {
		this.extractionConfiguration = extractionConfiguration;
	}
}
