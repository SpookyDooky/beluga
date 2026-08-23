package com.beluga.api.job.dto.read.extraction_configuration.text;

import org.junit.jupiter.api.Test;

import static com.beluga.model.job_definition.configuration.scraping_configuration.ExtractionType.TEXT;
import static org.junit.jupiter.api.Assertions.assertEquals;

class ReadTextExtractionConfigurationDtoTest {

    @Test
    void shouldHaveCorrectType() {
        final ReadTextExtractionConfigurationDto dto = new ReadTextExtractionConfigurationDto();

        assertEquals(TEXT, dto.getType());
    }
}