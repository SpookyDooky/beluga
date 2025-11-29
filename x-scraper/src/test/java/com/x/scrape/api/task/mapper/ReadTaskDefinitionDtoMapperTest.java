package com.x.scrape.api.task.mapper;

import com.x.scrape.api.task.dto.ReadTaskDefinitionDto;
import com.x.scrape.model.task.TaskDefinition;
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