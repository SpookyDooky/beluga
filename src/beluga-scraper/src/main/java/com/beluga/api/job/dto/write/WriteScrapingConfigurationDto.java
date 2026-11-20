package com.beluga.api.job.dto.write;

import com.beluga.api.job.dto.base.BaseScrapingConfigurationDto;
import com.beluga.api.job.dto.write.extraction_configuration.WriteExtractionConfigurationDto;
import com.beluga.api.job.validation.annotation.ValidFieldNames;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.List;

@ValidFieldNames
public class WriteScrapingConfigurationDto extends BaseScrapingConfigurationDto {
	
	@NotEmpty
	private List<@Valid @NotNull WriteExtractionConfigurationDto> dataPoints;
	
	public List<WriteExtractionConfigurationDto> getDataPoints() {
		return dataPoints;
	}
	
	public void setDataPoints(final List<WriteExtractionConfigurationDto> dataPoints) {
		this.dataPoints = dataPoints;
	}
}
