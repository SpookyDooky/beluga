package com.beluga.mapper.job.scraping_configuration.extraction_definition.attribute;

import com.beluga.api.job.dto.write.extraction_configuration.attribute.WriteAttributeExtractionConfigurationDto;
import com.beluga.model.job_definition.configuration.scraping_configuration.extraction_definition.attribute.AttributeExtractionDefinition;
import org.instancio.Instancio;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AttributeExtractionDefinitionMapperTest {

    private final AttributeExtractionDefinitionMapper mapper = new AttributeExtractionDefinitionMapperImpl();

    @Test
    void shouldMapFromWriteDto() {
        final WriteAttributeExtractionConfigurationDto dto = Instancio.create(WriteAttributeExtractionConfigurationDto.class);

        final AttributeExtractionDefinition definition = mapper.map(dto);

        assertEquals(dto.getAttribute(), definition.getAttribute());
        assertEquals(dto.getField(), definition.getField());
    }
}