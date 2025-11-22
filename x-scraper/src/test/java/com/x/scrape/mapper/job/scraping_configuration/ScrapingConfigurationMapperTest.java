package com.x.scrape.mapper.job.scraping_configuration;

import com.x.scrape.api.job.dto.read.ReadDataPointConfigurationDto;
import com.x.scrape.api.job.dto.read.ReadScrapingConfigurationDto;
import com.x.scrape.api.job.dto.write.WriteDataPointConfigurationDto;
import com.x.scrape.api.job.dto.write.WriteScrapingConfigurationDto;
import com.x.scrape.model.job_definition.configuration.scraping_configuration.DataPointConfiguration;
import com.x.scrape.model.job_definition.configuration.scraping_configuration.ScrapingConfiguration;
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
class ScrapingConfigurationMapperTest {

	@Mock
	private DataPointConfigurationMapper dataPointConfigurationMapper;
	
	@InjectMocks
	private ScrapingConfigurationMapperImpl mapper;
	
	@Test
	void shouldMap() {
		final ScrapingProperties dataScrapingProperties = Instancio.of(ScrapingProperties.class)
				.set(field(ScrapingProperties::getDataPoints), List.of(mock(DataPointProperties.class)))
				.create();
		
		final DataPointConfiguration dataPointConfiguration = mock();
		when(dataPointConfigurationMapper.map(dataScrapingProperties.getDataPoints().getFirst())).thenReturn(dataPointConfiguration);
		
		final ScrapingConfiguration scrapingConfiguration = mapper.map(dataScrapingProperties);
		
		assertEquals(dataScrapingProperties.getElementSelector(), scrapingConfiguration.getElementSelector());
		assertEquals(1, scrapingConfiguration.getDataPointConfigurations().size());
		assertSame(dataPointConfiguration, scrapingConfiguration.getDataPointConfigurations().getFirst());
	}
	
	@Test
	void shouldMapFromWriteScrapingConfigurationDto() {
		final WriteScrapingConfigurationDto dto = Instancio.of(WriteScrapingConfigurationDto.class)
				.set(field(WriteScrapingConfigurationDto::getDataPoints), List.of(mock(WriteDataPointConfigurationDto.class)))
				.create();
		
		final DataPointConfiguration dataPointConfiguration = mock();
		when(dataPointConfigurationMapper.map(dto.getDataPoints().getFirst())).thenReturn(dataPointConfiguration);
		
		final ScrapingConfiguration entity = mapper.map(dto);
		
		assertEquals(dto.getElementSelector(), entity.getElementSelector());
		assertEquals(1, entity.getDataPointConfigurations().size());
		assertSame(dataPointConfiguration, entity.getDataPointConfigurations().getFirst());
	}
	
	@Test
	void shouldMapToDto() {
		final ScrapingConfiguration entity = Instancio.of(ScrapingConfiguration.class)
				.set(field(ScrapingConfiguration::getDataPointConfigurations), List.of(mock(DataPointConfiguration.class)))
				.create();
		
		final ReadDataPointConfigurationDto dataPointConfigurationDto = mock();
		when(dataPointConfigurationMapper.map(entity.getDataPointConfigurations().getFirst())).thenReturn(dataPointConfigurationDto);
		
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
		
		final DataPointConfiguration dataPointConfiguration = mock();
		when(dataPointConfigurationMapper.map(dto.getDataPoints().getFirst())).thenReturn(dataPointConfiguration);
		
		final ScrapingConfiguration entity = Instancio.create(ScrapingConfiguration.class);
		
		mapper.update(dto, entity);
		
		assertEquals(1, entity.getDataPointConfigurations().size());
		assertSame(dataPointConfiguration, entity.getDataPointConfigurations().getFirst());
		assertEquals(dto.getElementSelector(), entity.getElementSelector());
	}
}