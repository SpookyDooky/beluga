package com.beluga.mapper.job.scraping_configuration;

import com.beluga.api.job.dto.read.ReadDataPointConfigurationDto;
import com.beluga.api.job.dto.read.ReadScrapingConfigurationDto;
import com.beluga.api.job.dto.write.WriteDataPointConfigurationDto;
import com.beluga.api.job.dto.write.WriteScrapingConfigurationDto;
import com.beluga.model.job_definition.configuration.scraping_configuration.DataPointDefinition;
import com.beluga.model.job_definition.configuration.scraping_configuration.ScrapingDefinition;
import com.beluga.properties.scraping.DataPointProperties;
import com.beluga.properties.scraping.ScrapingProperties;
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
		assertEquals(1, scrapingDefinition.getDataPointDefinitions().size());
		assertSame(dataPointDefinition, scrapingDefinition.getDataPointDefinitions().getFirst());
	}
	
	@Test
	void shouldMapFromWriteScrapingConfigurationDto() {
		final WriteScrapingConfigurationDto dto = Instancio.of(WriteScrapingConfigurationDto.class)
				.set(field(WriteScrapingConfigurationDto::getDataPoints), List.of(mock(WriteDataPointConfigurationDto.class)))
				.create();
		
		final DataPointDefinition dataPointDefinition = mock();
		when(dataPointDefinitionMapper.map(dto.getDataPoints().getFirst())).thenReturn(dataPointDefinition);
		
		final ScrapingDefinition entity = mapper.map(dto);
		
		assertEquals(dto.getItemSelector(), entity.getItemSelector());
		assertEquals(1, entity.getDataPointDefinitions().size());
		assertSame(dataPointDefinition, entity.getDataPointDefinitions().getFirst());
	}
	
	@Test
	void shouldMapToDto() {
		final ScrapingDefinition entity = Instancio.of(ScrapingDefinition.class)
				.set(field(ScrapingDefinition::getDataPointDefinitions), List.of(mock(DataPointDefinition.class)))
				.create();
		
		final ReadDataPointConfigurationDto dataPointConfigurationDto = mock();
		when(dataPointDefinitionMapper.map(entity.getDataPointDefinitions().getFirst())).thenReturn(dataPointConfigurationDto);
		
		final ReadScrapingConfigurationDto dto = mapper.map(entity);
		
		assertEquals(entity.getId(), dto.getId());
		assertEquals(entity.getItemSelector(), dto.getItemSelector());
		assertEquals(1, dto.getDataPoints().size());
		assertSame(dataPointConfigurationDto, dto.getDataPoints().getFirst());
	}
	
	@Test
	void shouldUpdate() {
		final WriteScrapingConfigurationDto dto = Instancio.of(WriteScrapingConfigurationDto.class)
				.set(field(WriteScrapingConfigurationDto::getDataPoints), List.of(mock(WriteDataPointConfigurationDto.class)))
				.create();
		
		final DataPointDefinition dataPointDefinition = mock();
		when(dataPointDefinitionMapper.map(dto.getDataPoints().getFirst())).thenReturn(dataPointDefinition);
		
		final ScrapingDefinition entity = Instancio.create(ScrapingDefinition.class);
		
		mapper.update(dto, entity);
		
		assertEquals(1, entity.getDataPointDefinitions().size());
		assertSame(dataPointDefinition, entity.getDataPointDefinitions().getFirst());
		assertEquals(dto.getItemSelector(), entity.getItemSelector());
	}
}