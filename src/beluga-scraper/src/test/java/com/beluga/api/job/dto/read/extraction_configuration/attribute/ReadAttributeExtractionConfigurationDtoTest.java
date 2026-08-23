package com.beluga.api.job.dto.read.extraction_configuration.attribute;

import org.junit.jupiter.api.Test;

import static com.beluga.model.job_definition.configuration.scraping_configuration.ExtractionType.ATTRIBUTE;
import static org.junit.jupiter.api.Assertions.assertEquals;

class ReadAttributeExtractionConfigurationDtoTest {

    @Test
    void shouldHaveCorrectType() {
        final ReadAttributeExtractionConfigurationDto dto = new ReadAttributeExtractionConfigurationDto();

        assertEquals(ATTRIBUTE, dto.getType());
    }
}