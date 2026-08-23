package com.beluga.mapper.job.scraping_configuration;

import com.beluga.api.job.dto.read.ReadScrapingConfigurationDto;
import com.beluga.api.job.dto.read.extraction_configuration.ReadExtractionConfigurationDto;
import com.beluga.api.job.dto.write.WriteScrapingConfigurationDto;
import com.beluga.api.job.dto.write.extraction_configuration.WriteExtractionConfigurationDto;
import com.beluga.mapper.job.scraping_configuration.extraction_definition.ExtractionDefinitionMapper;
import com.beluga.mapper.job.scraping_configuration.extraction_definition.ExtractionDefinitionMapperService;
import com.beluga.model.job_definition.configuration.scraping_configuration.ScrapingDefinition;
import com.beluga.model.job_definition.configuration.scraping_configuration.extraction_definition.ExtractionDefinition;
import com.beluga.properties.scraping.ScrapingProperties;
import org.instancio.Instancio;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.instancio.Select.field;
import static org.instancio.settings.Keys.COLLECTION_MAX_SIZE;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ScrapingDefinitionMapperTest {

    @Mock
    private ExtractionDefinitionMapper extractionDefinitionMapper;
	@Mock
    private ExtractionDefinitionMapperService extractionDefinitionMapperService;

    @InjectMocks
    private ScrapingDefinitionMapperImpl mapper;

    @Test
    void shouldMapFromProperties() {
        final ScrapingProperties properties = Instancio.of(ScrapingProperties.class)
                .withSetting(COLLECTION_MAX_SIZE, 1)
                .create();

        final ExtractionDefinition extractionDefinition = mock();
        when(extractionDefinitionMapperService.map(properties.getDataPoints().getFirst())).thenReturn(extractionDefinition);

        final ScrapingDefinition scrapingDefinition = mapper.map(properties);

        assertEquals(properties.getItemSelector(), scrapingDefinition.getItemSelector());
        assertEquals(1, scrapingDefinition.getExtractionDefinitions().size());
        assertTrue(scrapingDefinition.getExtractionDefinitions().contains(extractionDefinition));
    }

    @Test
    void shouldMapFromWriteScrapingConfigurationDto() {
        final WriteScrapingConfigurationDto dto = Instancio.of(WriteScrapingConfigurationDto.class)
                .set(field(WriteScrapingConfigurationDto::getDataPoints), List.of(mock(WriteExtractionConfigurationDto.class)))
                .create();

        final ExtractionDefinition extractionDefinition = mock();
        when(extractionDefinitionMapper.map(dto.getDataPoints().getFirst())).thenReturn(extractionDefinition);

        final ScrapingDefinition entity = mapper.map(dto);

        assertEquals(dto.getItemSelector(), entity.getItemSelector());
        assertEquals(1, entity.getExtractionDefinitions().size());
        assertSame(extractionDefinition, entity.getExtractionDefinitions().getFirst());
    }

    @Test
    void shouldMapToDto() {
        final ScrapingDefinition entity = Instancio.of(ScrapingDefinition.class)
                .set(field(ScrapingDefinition::getExtractionDefinitions), List.of(mock(ExtractionDefinition.class)))
                .create();

        final ReadExtractionConfigurationDto extractionConfigurationDto = mock();
        when(extractionDefinitionMapper.map(entity.getExtractionDefinitions().getFirst())).thenReturn(extractionConfigurationDto);

        final ReadScrapingConfigurationDto dto = mapper.map(entity);

        assertEquals(entity.getId(), dto.getId());
        assertEquals(entity.getItemSelector(), dto.getItemSelector());
        assertEquals(1, dto.getDataPoints().size());
        assertSame(extractionConfigurationDto, dto.getDataPoints().getFirst());
    }

    @Test
    void shouldUpdate() {
        final WriteScrapingConfigurationDto dto = Instancio.of(WriteScrapingConfigurationDto.class)
                .set(field(WriteScrapingConfigurationDto::getDataPoints), List.of(mock(WriteExtractionConfigurationDto.class)))
                .create();

        final ExtractionDefinition extractionDefinition = mock();
        when(extractionDefinitionMapper.map(dto.getDataPoints().getFirst())).thenReturn(extractionDefinition);

        final ScrapingDefinition entity = Instancio.create(ScrapingDefinition.class);

        mapper.update(dto, entity);

        assertEquals(1, entity.getExtractionDefinitions().size());
        assertSame(extractionDefinition, entity.getExtractionDefinitions().getFirst());
        assertEquals(dto.getItemSelector(), entity.getItemSelector());
    }
}