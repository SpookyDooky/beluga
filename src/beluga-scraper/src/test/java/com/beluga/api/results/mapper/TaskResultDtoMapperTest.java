package com.beluga.api.results.mapper;

import com.beluga.api.execution.exception.TaskResultNotFoundException;
import com.beluga.api.results.dto.TaskResultDto;
import com.beluga.execution.model.task.TaskExecution;
import com.beluga.model.result.ResultFile;
import com.beluga.result_storage.ResultStorageService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import tools.jackson.databind.ObjectMapper;

import java.nio.file.Path;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TaskResultDtoMapperTest {
	
	@Mock
	private ObjectMapper objectMapper;
	@Mock
	private ResultStorageService resultStorageService;
	
	@InjectMocks
	private TaskResultDtoMapper taskResultDtoMapper;
	
	@Test
	void shouldMap() throws Exception {
		final Long taskExecutionId = 1L;
		final TaskExecution taskExecution = mock(RETURNS_DEEP_STUBS);
		when(taskExecution.getId()).thenReturn(taskExecutionId);
		
		final ResultFile resultFile = mock();
		when(resultFile.getKey()).thenReturn("data.json");
		when(resultFile.getNamespace()).thenReturn("path");
		when(taskExecution.getResultFiles()).thenReturn(List.of(resultFile));
		
		final byte[] rawData = new byte[1];
		when(resultStorageService.retrieve(Path.of(resultFile.getNamespace()))).thenReturn(rawData);
		
		final List<Object> data = mock();
		when(objectMapper.readValue(rawData, List.class)).thenReturn(data);
		
		final TaskResultDto result = taskResultDtoMapper.map(taskExecution);
		
		assertEquals(taskExecutionId, result.getTaskId());
		assertSame(data, result.getData());
	}
	
	@Test
	void shouldThrowTaskResultNotFoundException() {
		final TaskExecution taskExecution = mock(RETURNS_DEEP_STUBS);
		when(taskExecution.getResultFiles()).thenReturn(List.of());
		
		assertThrows(TaskResultNotFoundException.class, () -> taskResultDtoMapper.map(taskExecution));
	}
	
}