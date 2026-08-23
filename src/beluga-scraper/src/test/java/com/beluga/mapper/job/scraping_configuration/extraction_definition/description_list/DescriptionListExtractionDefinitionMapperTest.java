package com.beluga.mapper.job.scraping_configuration.extraction_definition.description_list;

import com.beluga.api.job.dto.read.extraction_configuration.description_list.ReadDescriptionListExtractionConfigurationDto;
import com.beluga.api.job.dto.read.extraction_configuration.description_list.ReadDescriptionListExtractionDataPointConfigurationDto;
import com.beluga.api.job.dto.write.extraction_configuration.description_list.WriteDescriptionListExtractionConfigurationDto;
import com.beluga.model.job_definition.configuration.scraping_configuration.extraction_definition.description_list.DescriptionListExtractionDataPointDefinition;
import com.beluga.model.job_definition.configuration.scraping_configuration.extraction_definition.description_list.DescriptionListExtractionDefinition;
import com.beluga.properties.scraping.extraction_configuration.ExtractionConfigurationProperties;
import org.instancio.Instancio;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.instancio.settings.Keys.COLLECTION_MAX_SIZE;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DescriptionListExtractionDefinitionMapperTest {

    @Mock
    private DescriptionListDataPointDefinitionMapper descriptionListDataPointDefinitionMapper;

    @InjectMocks
    private DescriptionListExtractionDefinitionMapperImpl mapper;

    @Test
    void shouldMapFromWriteDto() {
        final WriteDescriptionListExtractionConfigurationDto dto = Instancio.of(WriteDescriptionListExtractionConfigurationDto.class)
                .withSetting(COLLECTION_MAX_SIZE, 1)
                .create();

        final DescriptionListExtractionDataPointDefinition dataPointDefinition = mock();
        when(descriptionListDataPointDefinitionMapper.map(dto.getDataPoints().getFirst())).thenReturn(dataPointDefinition);

        final DescriptionListExtractionDefinition definition = mapper.map(dto);

        assertEquals(dto.getSelector(), definition.getSelector());
        assertEquals(1, definition.getDataPoints().size());
        assertTrue(definition.getDataPoints().contains(dataPointDefinition));
    }

    @Test
    void shouldMapFromProperties() {
        final ExtractionConfigurationProperties properties = Instancio.of(ExtractionConfigurationProperties.class)
                .withSetting(COLLECTION_MAX_SIZE, 1)
                .create();

        final DescriptionListExtractionDataPointDefinition dataPointDefinition = mock();
        when(descriptionListDataPointDefinitionMapper.map(properties.getDataPoints().getFirst())).thenReturn(dataPointDefinition);

        final DescriptionListExtractionDefinition definition = mapper.map(properties);

        assertEquals(properties.getSelector(), definition.getSelector());
        assertEquals(1, definition.getDataPoints().size());
        assertTrue(definition.getDataPoints().contains(dataPointDefinition));
    }

    @Test
    void shouldMapFromExtractionDefinition() {
        final DescriptionListExtractionDefinition definition = Instancio.of(DescriptionListExtractionDefinition.class)
                .withSetting(COLLECTION_MAX_SIZE, 1)
                .create();

        final ReadDescriptionListExtractionDataPointConfigurationDto dataPointConfigurationDto = mock();
        when(descriptionListDataPointDefinitionMapper.map(definition.getDataPoints().getFirst())).thenReturn(dataPointConfigurationDto);

        final ReadDescriptionListExtractionConfigurationDto dto = mapper.map(definition);

        assertEquals(definition.getId(), dto.getId());
        assertEquals(definition.getDataPoints().size(), dto.getDataPoints().size());
        assertTrue(dto.getDataPoints().contains(dataPointConfigurationDto));
        assertEquals(definition.getSelector(), dto.getSelector());
    }
}