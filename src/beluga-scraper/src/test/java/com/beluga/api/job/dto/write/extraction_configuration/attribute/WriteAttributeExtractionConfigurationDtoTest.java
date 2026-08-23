package com.beluga.api.job.dto.write.extraction_configuration.attribute;

import org.junit.jupiter.api.Test;

import static com.beluga.model.job_definition.configuration.scraping_configuration.ExtractionType.ATTRIBUTE;
import static org.junit.jupiter.api.Assertions.assertEquals;

class WriteAttributeExtractionConfigurationDtoTest {

    @Test
    void shouldHaveCorrectType() {
        final WriteAttributeExtractionConfigurationDto dto = new WriteAttributeExtractionConfigurationDto();
        assertEquals(ATTRIBUTE, dto.getType());
    }
}