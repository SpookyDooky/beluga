package com.beluga.api.job.dto.read;

import com.beluga.api.job.dto.base.BaseScrapingConfigurationDto;
import com.beluga.api.job.dto.read.extraction_configuration.ReadExtractionConfigurationDto;

import java.util.List;

public class ReadScrapingConfigurationDto extends BaseScrapingConfigurationDto {
	
	private Long id;
	private List<ReadExtractionConfigurationDto> dataPoints;
	
	public Long getId() {
		return id;
	}
	
	public void setId(final Long id) {
		this.id = id;
	}
	
	public List<ReadExtractionConfigurationDto> getDataPoints() {
		return dataPoints;
	}
	
	public void setDataPoints(final List<ReadExtractionConfigurationDto> dataPoints) {
		this.dataPoints = dataPoints;
	}
}
