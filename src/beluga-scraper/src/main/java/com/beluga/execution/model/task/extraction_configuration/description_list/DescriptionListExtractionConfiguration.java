package com.beluga.execution.model.task.extraction_configuration.description_list;

import com.beluga.execution.model.task.extraction_configuration.ExtractionConfiguration;

import java.util.List;

import static com.beluga.model.job_definition.configuration.scraping_configuration.ExtractionType.DESCRIPTION_LIST;

public class DescriptionListExtractionConfiguration extends ExtractionConfiguration {

    private String selector;
    private List<DescriptionListExtractionDataPointConfiguration> dataPoints;

    public DescriptionListExtractionConfiguration() {
        super(DESCRIPTION_LIST);
    }

    public String getSelector() {
        return selector;
    }

    public void setSelector(final String selector) {
        this.selector = selector;
    }

    public List<DescriptionListExtractionDataPointConfiguration> getDataPoints() {
        return dataPoints;
    }

    public void setDataPoints(final List<DescriptionListExtractionDataPointConfiguration> dataPoints) {
        this.dataPoints = dataPoints;
    }
}
