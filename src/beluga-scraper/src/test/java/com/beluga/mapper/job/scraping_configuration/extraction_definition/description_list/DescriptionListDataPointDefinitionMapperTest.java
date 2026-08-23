package com.beluga.mapper.job.scraping_configuration.extraction_definition.description_list;

import com.beluga.api.job.dto.read.extraction_configuration.description_list.ReadDescriptionListExtractionDataPointConfigurationDto;
import com.beluga.api.job.dto.write.extraction_configuration.description_list.WriteDescriptionListExtractionDataPointConfigurationDto;
import com.beluga.model.job_definition.configuration.scraping_configuration.extraction_definition.description_list.DescriptionListExtractionDataPointDefinition;
import com.beluga.properties.scraping.extraction_configuration.DescriptionListExtractionConfigurationDataPointProperties;
import org.instancio.Instancio;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DescriptionListDataPointDefinitionMapperTest {

    private final DescriptionListDataPointDefinitionMapper mapper = new DescriptionListDataPointDefinitionMapperImpl();

    @Test
    void shouldMapFromWriteDto() {
        final WriteDescriptionListExtractionDataPointConfigurationDto dto = Instancio.create(WriteDescriptionListExtractionDataPointConfigurationDto.class);

        final DescriptionListExtractionDataPointDefinition definition = mapper.map(dto);

        assertEquals(dto.getDtValue(), definition.getDtValue());
        assertEquals(dto.getField(), definition.getField());
    }

    @Test
    void shouldMapFromProperties() {
        final DescriptionListExtractionConfigurationDataPointProperties properties = Instancio.create(DescriptionListExtractionConfigurationDataPointProperties.class);

        final DescriptionListExtractionDataPointDefinition definition = mapper.map(properties);

        assertEquals(properties.getDtValue(), definition.getDtValue());
        assertEquals(properties.getField(), definition.getField());
    }

    @Test
    void shouldMapFromDefinition() {
        final DescriptionListExtractionDataPointDefinition definition = Instancio.create(DescriptionListExtractionDataPointDefinition.class);

        final ReadDescriptionListExtractionDataPointConfigurationDto dto = mapper.map(definition);

        assertEquals(definition.getId(), dto.getId());
        assertEquals(definition.getField(), dto.getField());
        assertEquals(definition.getDtValue(), dto.getDtValue());
    }
}