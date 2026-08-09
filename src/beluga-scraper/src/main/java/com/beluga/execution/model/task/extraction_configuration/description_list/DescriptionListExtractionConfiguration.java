package com.beluga.execution.model.task.extraction_configuration.description_list;

import com.beluga.execution.model.task.extraction_configuration.ExtractionConfiguration;

import java.util.List;

import static com.beluga.model.job_definition.configuration.scraping_configuration.ExtractionType.DESCRIPTION_LIST;

public class DescriptionListExtractionConfiguration extends ExtractionConfiguration {

    private List<DescriptionListExtractionDataPointConfiguration> dataPoints;

    public DescriptionListExtractionConfiguration() {
        super(DESCRIPTION_LIST);
    }

    public List<DescriptionListExtractionDataPointConfiguration> getDataPoints() {
        return dataPoints;
    }

    public void setDataPoints(List<DescriptionListExtractionDataPointConfiguration> dataPoints) {
        this.dataPoints = dataPoints;
    }
}
