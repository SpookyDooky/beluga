package com.x.scrape.mapper.job.scraping_configuration;

import com.x.scrape.api.job.dto.read.ReadDataPointConfigurationDto;
import com.x.scrape.api.job.dto.read.ReadScrapingConfigurationDto;
import com.x.scrape.api.job.dto.write.WriteDataPointConfigurationDto;
import com.x.scrape.api.job.dto.write.WriteScrapingConfigurationDto;
import com.x.scrape.model.job_definition.configuration.scraping_configuration.DataPointDefinition;
import com.x.scrape.model.job_definition.configuration.scraping_configuration.ScrapingDefinition;
import com.x.scrape.properties.scraping.DataPointProperties;
import com.x.scrape.properties.scraping.ScrapingProperties;
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
	private DataPointConfigurationMapper dataPointConfigurationMapper;
	
	@InjectMocks
	private ScrapingConfigurationMapperImpl mapper;
	
	@Test
	void shouldMap() {
		final ScrapingProperties dataScrapingProperties = Instancio.of(ScrapingProperties.class)
				.set(field(ScrapingProperties::getDataPoints), List.of(mock(DataPointProperties.class)))
				.create();
		
		final DataPointDefinition dataPointDefinition = mock();
		when(dataPointConfigurationMapper.map(dataScrapingProperties.getDataPoints().getFirst())).thenReturn(dataPointDefinition);
		
		final ScrapingDefinition scrapingDefinition = mapper.map(dataScrapingProperties);
		
		assertEquals(dataScrapingProperties.getElementSelector(), scrapingDefinition.getElementSelector());
		assertEquals(1, scrapingDefinition.getDataPointDefinitions().size());
		assertSame(dataPointDefinition, scrapingDefinition.getDataPointDefinitions().getFirst());
	}
	
	@Test
	void shouldMapFromWriteScrapingConfigurationDto() {
		final WriteScrapingConfigurationDto dto = Instancio.of(WriteScrapingConfigurationDto.class)
				.set(field(WriteScrapingConfigurationDto::getDataPoints), List.of(mock(WriteDataPointConfigurationDto.class)))
				.create();
		
		final DataPointDefinition dataPointDefinition = mock();
		when(dataPointConfigurationMapper.map(dto.getDataPoints().getFirst())).thenReturn(dataPointDefinition);
		
		final ScrapingDefinition entity = mapper.map(dto);
		
		assertEquals(dto.getElementSelector(), entity.getElementSelector());
		assertEquals(1, entity.getDataPointDefinitions().size());
		assertSame(dataPointDefinition, entity.getDataPointDefinitions().getFirst());
	}
	
	@Test
	void shouldMapToDto() {
		final ScrapingDefinition entity = Instancio.of(ScrapingDefinition.class)
				.set(field(ScrapingDefinition::getDataPointDefinitions), List.of(mock(DataPointDefinition.class)))
				.create();
		
		final ReadDataPointConfigurationDto dataPointConfigurationDto = mock();
		when(dataPointConfigurationMapper.map(entity.getDataPointDefinitions().getFirst())).thenReturn(dataPointConfigurationDto);
		
		final ReadScrapingConfigurationDto dto = mapper.map(entity);
		
		assertEquals(entity.getId(), dto.getId());
		assertEquals(entity.getElementSelector(), dto.getElementSelector());
		assertEquals(1, dto.getDataPoints().size());
		assertSame(dataPointConfigurationDto, dto.getDataPoints().getFirst());
	}
	
	@Test
	void shouldUpdate() {
		final WriteScrapingConfigurationDto dto = Instancio.of(WriteScrapingConfigurationDto.class)
				.set(field(WriteScrapingConfigurationDto::getDataPoints), List.of(mock(WriteDataPointConfigurationDto.class)))
				.create();
		
		final DataPointDefinition dataPointDefinition = mock();
		when(dataPointConfigurationMapper.map(dto.getDataPoints().getFirst())).thenReturn(dataPointDefinition);
		
		final ScrapingDefinition entity = Instancio.create(ScrapingDefinition.class);
		
		mapper.update(dto, entity);
		
		assertEquals(1, entity.getDataPointDefinitions().size());
		assertSame(dataPointDefinition, entity.getDataPointDefinitions().getFirst());
		assertEquals(dto.getElementSelector(), entity.getElementSelector());
	}
}