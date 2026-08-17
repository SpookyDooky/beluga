package com.beluga.api.job.dto.write.extraction_configuration.attribute;

import com.beluga.api.job.dto.write.extraction_configuration.WriteExtractionConfigurationDto;
import jakarta.validation.constraints.NotBlank;

import static com.beluga.model.job_definition.configuration.scraping_configuration.ExtractionType.ATTRIBUTE;

public class WriteAttributeExtractionConfigurationDto extends WriteExtractionConfigurationDto {

    @NotBlank
    private String field;
    @NotBlank
    private String attribute;

    public WriteAttributeExtractionConfigurationDto() {
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
