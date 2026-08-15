package com.beluga.api.job.dto.read;

import com.beluga.api.job.dto.read.extraction_configuration.ReadExtractionConfigurationDto;

public class ReadDataPointConfigurationDto {
	
	private Long id;

	private String selector;
	private ReadExtractionConfigurationDto extraction;

	public Long getId() {
		return id;
	}
	
	public void setId(final Long id) {
		this.id = id;
	}

	public String getSelector() {
		return selector;
	}

	public void setSelector(final String selector) {
		this.selector = selector;
	}

	public ReadExtractionConfigurationDto getExtraction() {
		return extraction;
	}

	public void setExtraction(final ReadExtractionConfigurationDto extraction) {
		this.extraction = extraction;
	}
}
