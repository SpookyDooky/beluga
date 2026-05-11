package com.beluga.mapper.job.scraping_configuration;

import com.beluga.api.job.dto.read.ReadDataPointConfigurationDto;
import com.beluga.api.job.dto.write.WriteDataPointConfigurationDto;
import com.beluga.model.job_definition.configuration.scraping_configuration.DataPointDefinition;
import com.beluga.properties.scraping.DataPointProperties;
import org.instancio.Instancio;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class DataPointDefinitionMapperTest {
	
	private final DataPointConfigurationMapperImpl mapper = new DataPointConfigurationMapperImpl();
	
	@Test
	void shouldMap() {
		final DataPointProperties dataPointProperties = Instancio.create(DataPointProperties.class);
		
		final DataPointDefinition dataPointDefinition = mapper.map(dataPointProperties);
		
		assertEquals(dataPointProperties.getSelector(), dataPointDefinition.getSelector());
		assertEquals(dataPointProperties.getField(), dataPointDefinition.getField());
		assertEquals(dataPointProperties.getAttribute(), dataPointDefinition.getAttribute());
	}
	
	@Test
	void shouldMapFromWriteDataPointDto() {
		final WriteDataPointConfigurationDto dto = Instancio.create(WriteDataPointConfigurationDto.class);
		
		final DataPointDefinition entity = mapper.map(dto);
		
		assertEquals(dto.getSelector(), entity.getSelector());
		assertEquals(dto.getPropertyName(), entity.getField());
		assertEquals(dto.getAttribute(), entity.getAttribute());
		assertEquals(dto.getType(), entity.getType());
	}
	
	@Test
	void shouldMapToDto() {
		final DataPointDefinition entity = Instancio.create(DataPointDefinition.class);
		
		final ReadDataPointConfigurationDto dto = mapper.map(entity);
		
		assertEquals(entity.getId(), dto.getId());
		assertEquals(entity.getType(), dto.getType());
		assertEquals(entity.getSelector(), dto.getSelector());
		assertEquals(entity.getAttribute(), dto.getAttribute());
	}
}