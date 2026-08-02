package com.beluga.mapper.job.scraping_configuration.extraction_definition.html;

import com.beluga.api.job.dto.read.extraction_configuration.html.ReadHtmlExtractionConfigurationDto;
import com.beluga.api.job.dto.write.extraction_configuration.html.WriteHtmlExtractionConfigurationDto;
import com.beluga.model.job_definition.configuration.scraping_configuration.extraction_definition.html.HtmlExtractionDefinition;
import org.instancio.Instancio;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class HtmlExtractionDefinitionMapperTest {

    private final HtmlExtractionDefinitionMapper mapper = new HtmlExtractionDefinitionMapperImpl();

    @Test
    void shouldMapFromWriteDto() {
        final WriteHtmlExtractionConfigurationDto dto = Instancio.create(WriteHtmlExtractionConfigurationDto.class);

        final HtmlExtractionDefinition definition = mapper.map(dto);

        assertEquals(dto.getField(), definition.getField());
    }

    @Test
    void shouldMapFromExtractionDefinition() {
        final HtmlExtractionDefinition definition = Instancio.create(HtmlExtractionDefinition.class);

        final ReadHtmlExtractionConfigurationDto dto = mapper.map(definition);

        assertEquals(definition.getId(), dto.getId());
        assertEquals(definition.getField(), dto.getField());
    }
}