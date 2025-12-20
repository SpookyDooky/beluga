package com.x.scrape.api.execution.mapper;

import com.x.scrape.api.execution.dto.ReadTaskExecutionDto;
import com.x.scrape.model.task.TaskExecution;
import org.instancio.Instancio;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ReadTaskExecutionMapperTest {
	
	private final ReadTaskExecutionMapper mapper = new ReadTaskExecutionMapperImpl();
	
	@Test
	void shouldMap() {
		final TaskExecution taskExecution = Instancio.create(TaskExecution.class);
		
		final ReadTaskExecutionDto readTaskExecutionDto = mapper.map(taskExecution);
		
		assertEquals(taskExecution.getId(), readTaskExecutionDto.getId());
		assertEquals(taskExecution.getExecutedAt(), readTaskExecutionDto.getExecutedAt());
		assertEquals(taskExecution.getStatus(), readTaskExecutionDto.getStatus());
		assertEquals(taskExecution.getTaskDefinition().getUrl(), readTaskExecutionDto.getUrl());
	}
}