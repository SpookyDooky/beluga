package com.beluga.api.job.dto.write.extraction_configuration.text;

import org.junit.jupiter.api.Test;

import static com.beluga.model.job_definition.configuration.scraping_configuration.ExtractionType.TEXT;
import static org.junit.jupiter.api.Assertions.assertEquals;

class WriteTextExtractionConfigurationDtoTest {

    @Test
    void shouldHaveCorrectType() {
        final WriteTextExtractionConfigurationDto dto = new WriteTextExtractionConfigurationDto();
        assertEquals(TEXT, dto.getType());
    }
}