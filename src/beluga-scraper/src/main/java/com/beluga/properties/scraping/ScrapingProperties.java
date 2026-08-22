package com.beluga.properties.scraping;

import com.beluga.properties.scraping.extraction_configuration.ExtractionConfigurationProperties;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;

import java.util.List;

public class ScrapingProperties {
	
	@NotBlank
	private String itemSelector;
	private List<@Valid ExtractionConfigurationProperties> dataPoints;

	public String getItemSelector() {
		return itemSelector;
	}
	
	public void setItemSelector(final String itemSelector) {
		this.itemSelector = itemSelector;
	}

	public List<ExtractionConfigurationProperties> getDataPoints() {
		return dataPoints;
	}

	public void setDataPoints(final List<ExtractionConfigurationProperties> dataPoints) {
		this.dataPoints = dataPoints;
	}
}
