package com.beluga.mapper.task.configuration.extraction_configuration.attribute;

import com.beluga.execution.model.task.extraction_configuration.attribute.AttributeExtractionConfiguration;
import com.beluga.model.job_definition.configuration.scraping_configuration.extraction_definition.attribute.AttributeExtractionDefinition;
import org.instancio.Instancio;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AttributeExtractionConfigurationMapperTest {

    private final AttributeExtractionConfigurationMapper mapper = new AttributeExtractionConfigurationMapperImpl();

    @Test
    void shouldMap() {
        final AttributeExtractionDefinition definition = Instancio.create(AttributeExtractionDefinition.class);

        final AttributeExtractionConfiguration configuration = mapper.map(definition);

        assertEquals(definition.getSelector(), configuration.getSelector());
        assertEquals(definition.getAttribute(), configuration.getAttribute());
        assertEquals(definition.getField(), configuration.getField());
    }
}