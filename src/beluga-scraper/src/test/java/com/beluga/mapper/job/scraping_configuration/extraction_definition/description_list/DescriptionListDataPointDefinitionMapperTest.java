package com.beluga.mapper.job.scraping_configuration.extraction_definition.description_list;

import com.beluga.api.job.dto.write.extraction_configuration.description_list.WriteDescriptionListExtractionDataPointConfigurationDto;
import com.beluga.model.job_definition.configuration.scraping_configuration.extraction_definition.description_list.DescriptionListExtractionDataPointDefinition;
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
}