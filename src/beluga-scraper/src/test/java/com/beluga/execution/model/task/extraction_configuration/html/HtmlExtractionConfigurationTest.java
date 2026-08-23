package com.beluga.execution.model.task.extraction_configuration.html;

import org.junit.jupiter.api.Test;

import static com.beluga.model.job_definition.configuration.scraping_configuration.ExtractionType.HTML;
import static org.junit.jupiter.api.Assertions.assertEquals;

class HtmlExtractionConfigurationTest {

    @Test
    void shouldHaveCorrectType() {
        final HtmlExtractionConfiguration configuration = new HtmlExtractionConfiguration();

        assertEquals(HTML, configuration.getType());
    }
}