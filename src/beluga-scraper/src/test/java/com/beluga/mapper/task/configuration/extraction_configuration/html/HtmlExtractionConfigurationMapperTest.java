package com.beluga.mapper.task.configuration.extraction_configuration.html;

import com.beluga.execution.model.task.extraction_configuration.html.HtmlExtractionConfiguration;
import com.beluga.model.job_definition.configuration.scraping_configuration.extraction_definition.html.HtmlExtractionDefinition;
import org.instancio.Instancio;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class HtmlExtractionConfigurationMapperTest {

    private final HtmlExtractionConfigurationMapper mapper = new HtmlExtractionConfigurationMapperImpl();

    @Test
    void shouldMap() {
        final HtmlExtractionDefinition definition = Instancio.create(HtmlExtractionDefinition.class);

        final HtmlExtractionConfiguration configuration = mapper.map(definition);

        assertEquals(definition.getField(), configuration.getField());
    }
}