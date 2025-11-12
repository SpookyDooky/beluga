package com.x.scrape.api.job.dto.write;

import com.x.scrape.api.job.dto.base.BaseScrapingConfigurationDto;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;

import java.util.List;

public class WriteScrapingConfigurationDto extends BaseScrapingConfigurationDto {
	
	@NotEmpty
	private List<@Valid WriteDataPointConfigurationDto> dataPoints;
	
	public List<WriteDataPointConfigurationDto> getDataPoints() {
		return dataPoints;
	}
	
	public void setDataPoints(final List<WriteDataPointConfigurationDto> dataPoints) {
		this.dataPoints = dataPoints;
	}
}
