package com.beluga.execution.model.task.extraction_configuration.html;

import com.beluga.execution.model.task.extraction_configuration.ExtractionConfiguration;

import static com.beluga.model.job_definition.configuration.scraping_configuration.ExtractionType.HTML;

public class HtmlExtractionConfiguration extends ExtractionConfiguration {

    private String selector;
    private String field;

    public HtmlExtractionConfiguration() {
        super(HTML);
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
