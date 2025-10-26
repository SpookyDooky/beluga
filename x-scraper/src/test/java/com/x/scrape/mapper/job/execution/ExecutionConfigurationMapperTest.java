package com.x.scrape.mapper.job.execution;

import com.x.scrape.model.job.configuration.execution.ExecutionConfiguration;
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
}