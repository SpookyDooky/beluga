package com.beluga.api.job.dto.read.extraction_configuration.description_list;

import com.beluga.api.job.dto.read.extraction_configuration.ReadExtractionConfigurationDto;

import java.util.List;

import static com.beluga.model.job_definition.configuration.scraping_configuration.ExtractionType.DESCRIPTION_LIST;

public class ReadDescriptionListExtractionConfigurationDto extends ReadExtractionConfigurationDto {

    private String selector;
    private List<ReadDescriptionListExtractionDataPointConfigurationDto> dataPoints;

    public ReadDescriptionListExtractionConfigurationDto() {
        super(DESCRIPTION_LIST);
    }

    public String getSelector() {
        return selector;
    }

    public void setSelector(final String selector) {
        this.selector = selector;
    }

    public List<ReadDescriptionListExtractionDataPointConfigurationDto> getDataPoints() {
        return dataPoints;
    }

    public void setDataPoints(final List<ReadDescriptionListExtractionDataPointConfigurationDto> dataPoints) {
        this.dataPoints = dataPoints;
    }
}
