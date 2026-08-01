package com.beluga.mapper.job.scraping_configuration.extraction_definition.image;

import com.beluga.api.job.dto.write.extraction_configuration.image.WriteImageExtractionConfigurationDto;
import com.beluga.model.job_definition.configuration.scraping_configuration.extraction_definition.image.ImageExtractionDefinition;
import org.instancio.Instancio;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ImageExtractionDefinitionMapperTest {

    private final ImageExtractionDefinitionMapper mapper = new ImageExtractionDefinitionMapperImpl();

    @Test
    void shouldMapFromWriteDto() {
        final WriteImageExtractionConfigurationDto dto = Instancio.create(WriteImageExtractionConfigurationDto.class);

        final ImageExtractionDefinition definition = mapper.map(dto);

        assertNotNull(definition);
    }
}