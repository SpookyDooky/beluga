package com.beluga.mapper.job.scraping_configuration.extraction_definition.text;

import com.beluga.api.job.dto.read.extraction_configuration.text.ReadTextExtractionConfigurationDto;
import com.beluga.api.job.dto.write.extraction_configuration.text.WriteTextExtractionConfigurationDto;
import com.beluga.model.job_definition.configuration.scraping_configuration.extraction_definition.text.TextExtractionDefinition;
import org.instancio.Instancio;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TextExtractionDefinitionMapperTest {

    private final TextExtractionDefinitionMapper mapper = new TextExtractionDefinitionMapperImpl();

    @Test
    void shouldMapFromWriteDto() {
        final WriteTextExtractionConfigurationDto dto = Instancio.create(WriteTextExtractionConfigurationDto.class);

        final TextExtractionDefinition definition = mapper.map(dto);

        assertEquals(dto.getField(), definition.getField());
    }

    @Test
    void shouldMapFromExtractionDefinition() {
        final TextExtractionDefinition definition = Instancio.create(TextExtractionDefinition.class);

        final ReadTextExtractionConfigurationDto dto = mapper.map(definition);

        assertEquals(definition.getId(), dto.getId());
        assertEquals(definition.getField(), dto.getField());
    }
}