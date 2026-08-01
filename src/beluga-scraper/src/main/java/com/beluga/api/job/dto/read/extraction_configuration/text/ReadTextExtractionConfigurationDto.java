package com.beluga.api.job.dto.read.extraction_configuration.text;

import com.beluga.api.job.dto.read.extraction_configuration.ReadExtractionConfigurationDto;

import static com.beluga.model.job_definition.configuration.scraping_configuration.ExtractionType.TEXT;

public class ReadTextExtractionConfigurationDto extends ReadExtractionConfigurationDto {

    private String field;

    public ReadTextExtractionConfigurationDto() {
        super(TEXT);
    }

    public String getField() {
        return field;
    }

    public void setField(String field) {
        this.field = field;
    }
}
