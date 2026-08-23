package com.beluga.api.job.dto.write;

import com.beluga.api.job.dto.base.BaseScrapingConfigurationDto;
import com.beluga.api.job.dto.write.extraction_configuration.WriteExtractionConfigurationDto;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;

import java.util.List;

public class WriteScrapingConfigurationDto extends BaseScrapingConfigurationDto {
	
	@NotEmpty
	private List<@Valid WriteExtractionConfigurationDto> dataPoints;
	
	public List<WriteExtractionConfigurationDto> getDataPoints() {
		return dataPoints;
	}
	
	public void setDataPoints(final List<WriteExtractionConfigurationDto> dataPoints) {
		this.dataPoints = dataPoints;
	}
}
