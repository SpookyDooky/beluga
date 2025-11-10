package com.x.scrape.api.job.dto.write;

import com.x.scrape.api.job.dto.base.BaseScrapingDataPointDto;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;

import java.util.List;

public class WriteScrapingConfigurationDto extends BaseScrapingDataPointDto {
	
	@NotEmpty
	private List<@Valid WriteScrapingDataPointDto> dataPoints;
	
	public List<WriteScrapingDataPointDto> getDataPoints() {
		return dataPoints;
	}
	
	public void setDataPoints(final List<WriteScrapingDataPointDto> dataPoints) {
		this.dataPoints = dataPoints;
	}
}
