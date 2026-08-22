package com.beluga.mapper.job.scraping_configuration;

import com.beluga.api.job.dto.read.ReadScrapingConfigurationDto;
import com.beluga.api.job.dto.read.extraction_configuration.ReadExtractionConfigurationDto;
import com.beluga.api.job.dto.write.WriteScrapingConfigurationDto;
import com.beluga.mapper.job.scraping_configuration.extraction_definition.ExtractionDefinitionMapper;
import com.beluga.model.job_definition.configuration.scraping_configuration.ScrapingDefinition;
import com.beluga.model.job_definition.configuration.scraping_configuration.extraction_definition.ExtractionDefinition;
import com.beluga.properties.scraping.ScrapingProperties;
import org.assertj.core.api.Assertions;
import org.instancio.Instancio;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.instancio.Select.field;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ScrapingDefinitionMapperTest {

	@Mock
	private DataPointDefinitionMapper dataPointDefinitionMapper;
	@Mock
	private ExtractionDefinitionMapper extractionDefinitionMapper;

	@InjectMocks
	private ScrapingDefinitionMapperImpl mapper;
	
	@Test
	void shouldMap() {
		final ScrapingProperties dataScrapingProperties = Instancio.of(ScrapingProperties.class)
				.set(field(ScrapingProperties::getDataPoints), List.of(mock(DataPointProperties.class)))
				.create();
		
		final DataPointDefinition dataPointDefinition = mock();
		when(dataPointDefinitionMapper.map(dataScrapingProperties.getDataPoints().getFirst())).thenReturn(dataPointDefinition);
		
		final ScrapingDefinition scrapingDefinition = mapper.map(dataScrapingProperties);
		
		assertEquals(dataScrapingProperties.getItemSelector(), scrapingDefinition.getItemSelector());
		assertEquals(1, scrapingDefinition.getExtractionDefinitions().size());
		assertSame(dataPointDefinition, scrapingDefinition.getExtractionDefinitions().getFirst());

		Assertions.fail();
	}
	
	@Test
	void shouldMapFromWriteScrapingConfigurationDto() {
		final WriteScrapingConfigurationDto dto = Instancio.of(WriteScrapingConfigurationDto.class)
				.set(field(WriteScrapingConfigurationDto::getDataPoints), List.of(mock(WriteDataPointConfigurationDto.class)))
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
				.set(field(ScrapingDefinition::getExtractionDefinitions), List.of(mock(DataPointDefinition.class)))
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
				.set(field(WriteScrapingConfigurationDto::getDataPoints), List.of(mock(WriteDataPointConfigurationDto.class)))
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