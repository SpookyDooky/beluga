package com.beluga.execution.model.task.extraction_configuration.text;

import com.beluga.execution.model.task.extraction_configuration.ExtractionConfiguration;

import static com.beluga.model.job_definition.configuration.scraping_configuration.ExtractionType.TEXT;

public class TextExtractionConfiguration extends ExtractionConfiguration {

    private String selector;
    private String field;

    public TextExtractionConfiguration() {
        super(TEXT);
    }

    public String getSelector() {
        return selector;
    }

    public void setSelector(final String selector) {
        this.selector = selector;
    }

    public String getField() {
        return field;
    }

    public void setField(final String field) {
        this.field = field;
    }
}
