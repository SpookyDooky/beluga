package com.x.scrape.mapper.job.execution;

import com.x.scrape.api.job.dto.write.WriteExecutionConfigurationDto;
import com.x.scrape.model.job_definition.configuration.execution_configuration.ExecutionConfiguration;
import com.x.scrape.properties.scraping.execution.ExecutionProperties;
import org.instancio.Instancio;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ExecutionConfigurationMapperTest {
	
	private final ExecutionConfigurationMapper mapper = new ExecutionConfigurationMapperImpl();
	
	@Test
	void shouldMap() {
		final ExecutionProperties executionProperties = Instancio.create(ExecutionProperties.class);
		
		final ExecutionConfiguration executionConfiguration = mapper.map(executionProperties);
		
		assertEquals(executionProperties.getWorkers(), executionConfiguration.getWorkers());
		assertEquals(executionProperties.getTasksPerSecond(), executionConfiguration.getTasksPerSecond());
	}
	
	@Test
	void shouldMapFromWriteExecutionConfigurationDto() {
		final WriteExecutionConfigurationDto dto = Instancio.create(WriteExecutionConfigurationDto.class);
		
		final ExecutionConfiguration entity = mapper.map(dto);
		
		assertEquals(dto.getWorkers(), entity.getWorkers());
		assertEquals(dto.getTasksPerSecond(), entity.getTasksPerSecond());
	}
}