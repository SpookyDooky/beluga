package com.beluga.mapper.task.configuration.extraction_configuration.text;

import com.beluga.execution.model.task.extraction_configuration.text.TextExtractionConfiguration;
import com.beluga.model.job_definition.configuration.scraping_configuration.extraction_definition.text.TextExtractionDefinition;
import org.instancio.Instancio;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TextExtractionConfigurationMapperTest {

    private final TextExtractionConfigurationMapper mapper = new TextExtractionConfigurationMapperImpl();

    @Test
    void shouldMap() {
        final TextExtractionDefinition definition = Instancio.create(TextExtractionDefinition.class);

        final TextExtractionConfiguration configuration = mapper.map(definition);

        assertEquals(definition.getSelector(), configuration.getSelector());
        assertEquals(definition.getField(), configuration.getField());
    }
}