package com.beluga.api.job.dto.read.extraction_configuration;

import com.beluga.model.job_definition.configuration.scraping_configuration.ExtractionType;

public abstract class ReadExtractionConfigurationDto {

    private final ExtractionType type;

    protected ReadExtractionConfigurationDto(final ExtractionType type) {
        this.type = type;
    }

    public ExtractionType getType() {
        return type;
    }
}
