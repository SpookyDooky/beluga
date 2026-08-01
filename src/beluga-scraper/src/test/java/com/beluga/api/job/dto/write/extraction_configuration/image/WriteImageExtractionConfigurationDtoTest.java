package com.beluga.api.job.dto.write.extraction_configuration.image;

import org.junit.jupiter.api.Test;

import static com.beluga.model.job_definition.configuration.scraping_configuration.ExtractionType.IMAGE;
import static org.junit.jupiter.api.Assertions.assertEquals;

class WriteImageExtractionConfigurationDtoTest {

    @Test
    void shouldHaveCorrectType() {
        final WriteImageExtractionConfigurationDto dto = new WriteImageExtractionConfigurationDto();
        assertEquals(IMAGE, dto.getType());
    }
}