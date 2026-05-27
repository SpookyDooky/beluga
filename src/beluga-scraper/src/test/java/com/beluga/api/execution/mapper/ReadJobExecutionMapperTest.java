package com.beluga.api.execution.mapper;

import com.beluga.api.execution.dto.ReadJobExecutionDto;
import com.beluga.api.execution.dto.ReadJobExecutionWithTasksDto;
import com.beluga.api.execution.dto.ReadTaskExecutionDto;
import com.beluga.execution.model.task.TaskExecution;
import com.beluga.model.job_definition.JobExecution;
import org.instancio.Instancio;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.instancio.Select.field;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ReadJobExecutionMapperTest {
	
	@Mock
	private ReadTaskExecutionMapper readTaskExecutionMapper;
	
	@InjectMocks
	private ReadJobExecutionMapperImpl mapper;
	
	@Test
	void shouldMap() {
		final JobExecution jobExecution = Instancio.create(JobExecution.class);
		
		final ReadJobExecutionDto readJobExecutionDto = mapper.map(jobExecution);
		
		assertEquals(jobExecution.getId(), readJobExecutionDto.getId());
		assertEquals(jobExecution.getExecutedAt(), readJobExecutionDto.getExecutedAt());
		assertEquals(jobExecution.getStatus(), readJobExecutionDto.getStatus());
	}
	
	@Test
	void shouldMapWithTasks() {
		final TaskExecution taskExecution = mock();
		final ReadTaskExecutionDto readTaskExecutionDto = mock();
		when(readTaskExecutionMapper.map(taskExecution)).thenReturn(readTaskExecutionDto);
		
		final JobExecution jobExecution = Instancio.of(JobExecution.class)
				.set(field(JobExecution::getTasks), List.of(taskExecution))
				.create();
		
		final ReadJobExecutionWithTasksDto readJobExecutionDto = mapper.mapWithTasks(jobExecution);
		
		assertEquals(jobExecution.getId(), readJobExecutionDto.getId());
		assertEquals(jobExecution.getExecutedAt(), readJobExecutionDto.getExecutedAt());
		assertEquals(jobExecution.getStatus(), readJobExecutionDto.getStatus());
		assertEquals(List.of(readTaskExecutionDto), readJobExecutionDto.getTasks());
	}
}