package com.beluga.execution.model.task.extraction_configuration.text;

import com.beluga.execution.model.task.extraction_configuration.ExtractionConfiguration;

import static com.beluga.model.job_definition.configuration.scraping_configuration.ExtractionType.TEXT;

public class TextExtractionConfiguration extends ExtractionConfiguration {

    private String field;

    public TextExtractionConfiguration() {
        super(TEXT);
    }

    public String getField() {
        return field;
    }

    public void setField(String field) {
        this.field = field;
    }
}
