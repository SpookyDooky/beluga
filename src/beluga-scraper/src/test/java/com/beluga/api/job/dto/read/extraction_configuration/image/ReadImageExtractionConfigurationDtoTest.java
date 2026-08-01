package com.beluga.api.job.dto.read.extraction_configuration.image;

import org.junit.jupiter.api.Test;

import static com.beluga.model.job_definition.configuration.scraping_configuration.ExtractionType.IMAGE;
import static org.junit.jupiter.api.Assertions.assertEquals;

class ReadImageExtractionConfigurationDtoTest {

    @Test
    void shouldHaveCorrectType() {
        final ReadImageExtractionConfigurationDto dto = new ReadImageExtractionConfigurationDto();

        assertEquals(IMAGE, dto.getType());
    }
}