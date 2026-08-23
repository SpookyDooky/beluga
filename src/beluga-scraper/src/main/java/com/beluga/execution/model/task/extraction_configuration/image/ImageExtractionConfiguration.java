package com.beluga.execution.model.task.extraction_configuration.image;

import com.beluga.execution.model.task.extraction_configuration.ExtractionConfiguration;

import static com.beluga.model.job_definition.configuration.scraping_configuration.ExtractionType.IMAGE;

public class ImageExtractionConfiguration extends ExtractionConfiguration {

    public ImageExtractionConfiguration() {
        super(IMAGE);
    }
}
