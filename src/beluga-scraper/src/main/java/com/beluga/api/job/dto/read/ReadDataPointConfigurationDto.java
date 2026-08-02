package com.beluga.api.job.dto.read;

import com.beluga.api.job.dto.read.extraction_configuration.ReadExtractionConfigurationDto;

import java.util.List;

public class ReadDataPointConfigurationDto {
	
	private Long id;

	private String selector;
	private List<ReadExtractionConfigurationDto> extraction;

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

	public List<ReadExtractionConfigurationDto> getExtraction() {
		return extraction;
	}

	public void setExtraction(final List<ReadExtractionConfigurationDto> extraction) {
		this.extraction = extraction;
	}
}
