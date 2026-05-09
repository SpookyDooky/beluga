package com.x.scrape.api.job.dto.read;

import com.x.scrape.api.job.dto.base.BaseScrapingConfigurationDto;

import java.util.List;

public class ReadScrapingConfigurationDto extends BaseScrapingConfigurationDto {
	
	private Long id;
	private List<ReadDataPointConfigurationDto> dataPoints;
	
	public Long getId() {
		return id;
	}
	
	public void setId(final Long id) {
		this.id = id;
	}
	
	public List<ReadDataPointConfigurationDto> getDataPoints() {
		return dataPoints;
	}
	
	public void setDataPoints(final List<ReadDataPointConfigurationDto> dataPoints) {
		this.dataPoints = dataPoints;
	}
}
