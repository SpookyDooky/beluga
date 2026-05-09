package com.beluga.api.task.mapper;

import com.beluga.api.task.dto.ReadTaskDefinitionDto;
import com.beluga.execution.model.task.TaskDefinition;
import org.instancio.Instancio;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ReadTaskDefinitionDtoMapperTest {
	
	private final ReadTaskDefinitionDtoMapper mapper = new ReadTaskDefinitionDtoMapperImpl();
	
	@Test
	void shouldMap() {
		final TaskDefinition taskDefinition = Instancio.create(TaskDefinition.class);
		
		final ReadTaskDefinitionDto dto = mapper.map(taskDefinition);
		
		assertEquals(taskDefinition.getId(), dto.getId());
		assertEquals(taskDefinition.getUrl(), dto.getUrl());
	}
}