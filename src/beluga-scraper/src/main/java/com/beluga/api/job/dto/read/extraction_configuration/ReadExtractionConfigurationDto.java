package com.beluga.api.job.dto.read.extraction_configuration;

import com.beluga.model.job_definition.configuration.scraping_configuration.ExtractionType;

public abstract class ReadExtractionConfigurationDto {

    private Long id;
    private final ExtractionType type;

    protected ReadExtractionConfigurationDto(final ExtractionType type) {
        this.type = type;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ExtractionType getType() {
        return type;
    }
}
