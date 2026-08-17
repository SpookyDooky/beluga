package com.beluga.execution.model.task.extraction_configuration.attribute;

import com.beluga.execution.model.task.extraction_configuration.ExtractionConfiguration;

import static com.beluga.model.job_definition.configuration.scraping_configuration.ExtractionType.ATTRIBUTE;

public class AttributeExtractionConfiguration extends ExtractionConfiguration {

    private String field;
    private String attribute;

    public AttributeExtractionConfiguration() {
        super(ATTRIBUTE);
    }

    public String getField() {
        return field;
    }

    public void setField(final String field) {
        this.field = field;
    }

    public String getAttribute() {
        return attribute;
    }

    public void setAttribute(final String attribute) {
        this.attribute = attribute;
    }
}
