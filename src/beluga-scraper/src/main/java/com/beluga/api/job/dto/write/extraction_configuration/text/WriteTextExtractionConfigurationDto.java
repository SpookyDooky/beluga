package com.beluga.api.job.dto.write.extraction_configuration.text;

import com.beluga.api.job.dto.write.extraction_configuration.WriteExtractionConfigurationDto;
import jakarta.validation.constraints.NotBlank;

import static com.beluga.model.job_definition.configuration.scraping_configuration.ExtractionType.TEXT;

public class WriteTextExtractionConfigurationDto extends WriteExtractionConfigurationDto {

    @NotBlank
    private String field;

    public WriteTextExtractionConfigurationDto() {
        super(TEXT);
    }

    public String getField() {
        return field;
    }

    public void setField(final String field) {
        this.field = field;
    }
}
