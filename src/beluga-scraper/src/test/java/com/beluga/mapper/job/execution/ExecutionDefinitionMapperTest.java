package com.beluga.mapper.job.execution;

import com.beluga.api.job.dto.read.ReadExecutionConfigurationDto;
import com.beluga.api.job.dto.write.WriteExecutionConfigurationDto;
import com.beluga.model.job_definition.configuration.execution_configuration.ExecutionDefinition;
import com.beluga.properties.scraping.execution.ExecutionProperties;
import org.instancio.Instancio;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ExecutionDefinitionMapperTest {
	
	private final ExecutionDefinitionMapper mapper = new ExecutionDefinitionMapperImpl();
	
	@Test
	void shouldMap() {
		final ExecutionProperties executionProperties = Instancio.create(ExecutionProperties.class);
		
		final ExecutionDefinition executionDefinition = mapper.map(executionProperties);
		
		assertEquals(executionProperties.getWorkers(), executionDefinition.getWorkers());
		assertEquals(executionProperties.getTasksPerSecond(), executionDefinition.getTasksPerSecond());
	}
	
	@Test
	void shouldMapFromWriteExecutionConfigurationDto() {
		final WriteExecutionConfigurationDto dto = Instancio.create(WriteExecutionConfigurationDto.class);
		
		final ExecutionDefinition entity = mapper.map(dto);
		
		assertEquals(dto.getWorkers(), entity.getWorkers());
		assertEquals(dto.getTasksPerSecond(), entity.getTasksPerSecond());
	}
	
	@Test
	void shouldMapToDto() {
		final ExecutionDefinition entity = Instancio.create(ExecutionDefinition.class);
		
		final ReadExecutionConfigurationDto dto = mapper.map(entity);
		
		assertEquals(entity.getId(), dto.getId());
		assertEquals(entity.getWorkers(), dto.getWorkers());
		assertEquals(entity.getTasksPerSecond(), dto.getTasksPerSecond());
	}
	
	@Test
	void shouldUpdate() {
		final WriteExecutionConfigurationDto dto = Instancio.create(WriteExecutionConfigurationDto.class);
		final ExecutionDefinition entity = Instancio.create(ExecutionDefinition.class);
		
		mapper.update(dto, entity);
		
		assertEquals(dto.getWorkers(), entity.getWorkers());
		assertEquals(dto.getTasksPerSecond(), entity.getTasksPerSecond());
	}
}