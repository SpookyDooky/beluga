package com.x.scrape.api.results.mapper;

import com.x.scrape.api.execution.exception.TaskResultNotFoundException;
import com.x.scrape.api.results.dto.TaskResultDto;
import com.x.scrape.execution.model.task.TaskExecution;
import com.x.scrape.model.result.ResultFile;
import com.x.scrape.result_storage.StorageService;
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
	private StorageService storageService;
	
	@InjectMocks
	private TaskResultDtoMapper taskResultDtoMapper;
	
	@Test
	void shouldMap() throws Exception {
		final Long taskExecutionId = 1L;
		final TaskExecution taskExecution = mock(RETURNS_DEEP_STUBS);
		when(taskExecution.getId()).thenReturn(taskExecutionId);
		
		final ResultFile resultFile = mock();
		when(resultFile.getFileName()).thenReturn("data.json");
		when(resultFile.getPath()).thenReturn("path");
		when(taskExecution.getResultFiles()).thenReturn(List.of(resultFile));
		
		final byte[] rawData = new byte[1];
		when(storageService.retrieve(Path.of(resultFile.getPath()))).thenReturn(rawData);
		
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