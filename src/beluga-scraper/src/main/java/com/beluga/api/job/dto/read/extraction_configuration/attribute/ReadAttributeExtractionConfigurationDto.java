package com.beluga.api.job.dto.read.extraction_configuration.attribute;

import com.beluga.api.job.dto.read.extraction_configuration.ReadExtractionConfigurationDto;

import static com.beluga.model.job_definition.configuration.scraping_configuration.ExtractionType.ATTRIBUTE;

public class ReadAttributeExtractionConfigurationDto extends ReadExtractionConfigurationDto {

    private String field;
    private String attribute;

    public ReadAttributeExtractionConfigurationDto() {
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
