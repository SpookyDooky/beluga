package com.beluga.execution.model.task.extraction_configuration.attribute;

import org.junit.jupiter.api.Test;

import static com.beluga.model.job_definition.configuration.scraping_configuration.ExtractionType.ATTRIBUTE;
import static org.junit.jupiter.api.Assertions.assertEquals;

class AttributeExtractionConfigurationTest {

    @Test
    void shouldHaveCorrectType() {
        final AttributeExtractionConfiguration configuration = new AttributeExtractionConfiguration();

        assertEquals(ATTRIBUTE, configuration.getType());
    }
}