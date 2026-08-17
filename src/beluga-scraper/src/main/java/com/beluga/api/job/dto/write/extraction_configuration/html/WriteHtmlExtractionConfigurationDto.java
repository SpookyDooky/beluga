package com.beluga.api.job.dto.write.extraction_configuration.html;

import com.beluga.api.job.dto.write.extraction_configuration.WriteExtractionConfigurationDto;

import static com.beluga.model.job_definition.configuration.scraping_configuration.ExtractionType.HTML;

public class WriteHtmlExtractionConfigurationDto extends WriteExtractionConfigurationDto {

    private String field;

    public WriteHtmlExtractionConfigurationDto() {
        super(HTML);
    }

    public String getField() {
        return field;
    }

    public void setField(final String field) {
        this.field = field;
    }
}
