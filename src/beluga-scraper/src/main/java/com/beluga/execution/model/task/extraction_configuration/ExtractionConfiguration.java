package com.beluga.execution.model.task.extraction_configuration;

import com.beluga.model.job_definition.configuration.scraping_configuration.ExtractionType;

public abstract class ExtractionConfiguration {

    private final ExtractionType type;

    protected ExtractionConfiguration(final ExtractionType type) {
        this.type = type;
    }

    public ExtractionType getType() {
        return type;
    }
}
