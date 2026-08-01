package com.beluga.api.job.dto.read.extraction_configuration.html;

import com.beluga.api.job.dto.read.extraction_configuration.ReadExtractionConfigurationDto;

import static com.beluga.model.job_definition.configuration.scraping_configuration.ExtractionType.HTML;

public class ReadHtmlExtractionConfigurationDto extends ReadExtractionConfigurationDto {

    private String field;

    public ReadHtmlExtractionConfigurationDto() {
        super(HTML);
    }

    public String getField() {
        return field;
    }

    public void setField(String field) {
        this.field = field;
    }
}
