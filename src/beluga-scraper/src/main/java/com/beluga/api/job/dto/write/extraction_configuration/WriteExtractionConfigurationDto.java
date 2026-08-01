package com.beluga.api.job.dto.write.extraction_configuration;

import com.beluga.model.job_definition.configuration.scraping_configuration.ExtractionType;

public abstract class WriteExtractionConfigurationDto {

    private final ExtractionType type;

    protected WriteExtractionConfigurationDto(final ExtractionType type) {
        this.type = type;
    }

    public ExtractionType getType() {
        return type;
    }
}
