package com.beluga.execution.model.task.extraction_configuration.text;

import org.junit.jupiter.api.Test;

import static com.beluga.model.job_definition.configuration.scraping_configuration.ExtractionType.TEXT;
import static org.junit.jupiter.api.Assertions.assertEquals;

class TextExtractionConfigurationTest {

    @Test
    void shouldHaveCorrectType() {
        final TextExtractionConfiguration configuration = new TextExtractionConfiguration();

        assertEquals(TEXT, configuration.getType());
    }
}