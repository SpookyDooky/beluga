package com.beluga.api.job.dto.read.extraction_configuration.html;

import com.beluga.api.job.dto.read.extraction_configuration.ReadExtractionConfigurationDto;

import static com.beluga.model.job_definition.configuration.scraping_configuration.ExtractionType.HTML;

public class ReadHtmlExtractionConfigurationDto extends ReadExtractionConfigurationDto {

    private String selector;
    private String field;

    public ReadHtmlExtractionConfigurationDto() {
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
