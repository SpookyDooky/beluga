package com.beluga.mapper.task.configuration.extraction_configuration.image;

import com.beluga.execution.model.task.extraction_configuration.image.ImageExtractionConfiguration;
import com.beluga.model.job_definition.configuration.scraping_configuration.extraction_definition.image.ImageExtractionDefinition;
import org.instancio.Instancio;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ImageExtractionConfigurationMapperTest {

    private final ImageExtractionConfigurationMapper mapper = new ImageExtractionConfigurationMapperImpl();

    @Test
    void shouldMap() {
        final ImageExtractionDefinition definition = Instancio.create(ImageExtractionDefinition.class);

        final ImageExtractionConfiguration configuration = mapper.map(definition);

        assertNotNull(configuration);
    }
}