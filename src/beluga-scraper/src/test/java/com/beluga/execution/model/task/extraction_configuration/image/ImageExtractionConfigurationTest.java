package com.beluga.execution.model.task.extraction_configuration.image;

import org.junit.jupiter.api.Test;

import static com.beluga.model.job_definition.configuration.scraping_configuration.ExtractionType.IMAGE;
import static org.junit.jupiter.api.Assertions.assertEquals;

class ImageExtractionConfigurationTest {

    @Test
    void shouldHaveCorrectType() {
        final ImageExtractionConfiguration configuration = new ImageExtractionConfiguration();

        assertEquals(IMAGE, configuration.getType());
    }
}