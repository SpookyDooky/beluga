package com.beluga.mapper.job.scraping_configuration;

import com.beluga.api.job.dto.read.ReadDataPointConfigurationDto;
import com.beluga.api.job.dto.read.extraction_configuration.ReadExtractionConfigurationDto;
import com.beluga.api.job.dto.write.WriteDataPointConfigurationDto;
import com.beluga.mapper.job.scraping_configuration.extraction_definition.ExtractionDefinitionMapper;
import com.beluga.model.job_definition.configuration.scraping_configuration.extraction_definition.ExtractionDefinition;
import org.instancio.Instancio;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DataPointDefinitionMapperTest {

	@Mock
	private ExtractionDefinitionMapper extractionDefinitionMapper;

	@InjectMocks
	private DataPointDefinitionMapperImpl mapper;
	
	@Test
	void shouldMap() {
		final DataPointProperties dataPointProperties = Instancio.create(DataPointProperties.class);
		
		final DataPointDefinition dataPointDefinition = mapper.map(dataPointProperties);
		
		assertEquals(dataPointProperties.getSelector(), dataPointDefinition.getSelector());
	}
	
	@Test
	void shouldMapFromWriteDataPointDto() {
		final WriteDataPointConfigurationDto dto = Instancio.create(WriteDataPointConfigurationDto.class);

		final ExtractionDefinition extractionDefinition = mock();
		when(extractionDefinitionMapper.map(dto.getExtraction())).thenReturn(extractionDefinition);

		final DataPointDefinition entity = mapper.map(dto);
		
		assertEquals(dto.getSelector(), entity.getSelector());
		assertEquals(extractionDefinition, entity.getExtractionDefinition());
	}
	
	@Test
	void shouldMapToDto() {
		final DataPointDefinition entity = Instancio.create(DataPointDefinition.class);

		final ReadExtractionConfigurationDto configurationDto = mock();
		when(extractionDefinitionMapper.map(entity.getExtractionDefinition())).thenReturn(configurationDto);

		final ReadDataPointConfigurationDto dto = mapper.map(entity);
		
		assertEquals(entity.getId(), dto.getId());
		assertEquals(entity.getSelector(), dto.getSelector());
		assertSame(configurationDto, dto.getExtraction());
	}
}