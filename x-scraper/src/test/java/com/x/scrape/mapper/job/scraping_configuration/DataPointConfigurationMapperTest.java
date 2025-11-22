package com.x.scrape.mapper.job.scraping_configuration;

import com.x.scrape.api.job.dto.read.ReadDataPointConfigurationDto;
import com.x.scrape.api.job.dto.write.WriteDataPointConfigurationDto;
import com.x.scrape.model.job_definition.configuration.scraping_configuration.DataPointConfiguration;
import com.x.scrape.properties.scraping.DataPointProperties;
import org.instancio.Instancio;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class DataPointConfigurationMapperTest {
	
	private final DataPointConfigurationMapperImpl mapper = new DataPointConfigurationMapperImpl();
	
	@Test
	void shouldMap() {
		final DataPointProperties dataPointProperties = Instancio.create(DataPointProperties.class);
		
		final DataPointConfiguration dataPointConfiguration = mapper.map(dataPointProperties);
		
		assertEquals(dataPointProperties.getSelector(), dataPointConfiguration.getSelector());
		assertEquals(dataPointProperties.getPropertyName(), dataPointConfiguration.getPropertyName());
		assertEquals(dataPointProperties.getAttribute(), dataPointConfiguration.getAttribute());
	}
	
	@Test
	void shouldMapFromWriteDataPointDto() {
		final WriteDataPointConfigurationDto dto = Instancio.create(WriteDataPointConfigurationDto.class);
		
		final DataPointConfiguration entity = mapper.map(dto);
		
		assertEquals(dto.getSelector(), entity.getSelector());
		assertEquals(dto.getPropertyName(), entity.getPropertyName());
		assertEquals(dto.getAttribute(), entity.getAttribute());
		assertEquals(dto.getType(), entity.getType());
	}
	
	@Test
	void shouldMapToDto() {
		final DataPointConfiguration entity = Instancio.create(DataPointConfiguration.class);
		
		final ReadDataPointConfigurationDto dto = mapper.map(entity);
		
		assertEquals(entity.getId(), dto.getId());
		assertEquals(entity.getType(), dto.getType());
		assertEquals(entity.getSelector(), dto.getSelector());
		assertEquals(entity.getAttribute(), dto.getAttribute());
	}
}