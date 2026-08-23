package com.beluga.api.job.dto.write.extraction_configuration.html;

import org.junit.jupiter.api.Test;

import static com.beluga.model.job_definition.configuration.scraping_configuration.ExtractionType.HTML;
import static org.junit.jupiter.api.Assertions.assertEquals;

class WriteHtmlExtractionConfigurationDtoTest {

    @Test
    void shouldHaveCorrectType() {
        final WriteHtmlExtractionConfigurationDto dto = new WriteHtmlExtractionConfigurationDto();
        assertEquals(HTML, dto.getType());
    }
}