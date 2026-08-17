package com.beluga.api.job.dto.read.extraction_configuration.description_list;

import com.beluga.api.job.dto.read.extraction_configuration.ReadExtractionConfigurationDto;

import java.util.List;

import static com.beluga.model.job_definition.configuration.scraping_configuration.ExtractionType.DESCRIPTION_LIST;

public class ReadDescriptionListExtractionConfigurationDto extends ReadExtractionConfigurationDto {

    private List<ReadDescriptionListExtractionDataPointConfigurationDto> dataPoints;

    public ReadDescriptionListExtractionConfigurationDto() {
        super(DESCRIPTION_LIST);
    }

    public List<ReadDescriptionListExtractionDataPointConfigurationDto> getDataPoints() {
        return dataPoints;
    }

    public void setDataPoints(final List<ReadDescriptionListExtractionDataPointConfigurationDto> dataPoints) {
        this.dataPoints = dataPoints;
    }
}
