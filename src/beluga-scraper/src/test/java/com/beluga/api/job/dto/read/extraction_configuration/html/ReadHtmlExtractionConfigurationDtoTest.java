package com.beluga.api.job.dto.read.extraction_configuration.html;

import org.junit.jupiter.api.Test;

import static com.beluga.model.job_definition.configuration.scraping_configuration.ExtractionType.HTML;
import static org.junit.jupiter.api.Assertions.assertEquals;

class ReadHtmlExtractionConfigurationDtoTest {

    @Test
    void shouldHaveCorrectType() {
        final ReadHtmlExtractionConfigurationDto dto = new ReadHtmlExtractionConfigurationDto();

        assertEquals(HTML, dto.getType());
    }
}