package com.beluga.api.results.mapper;

import com.beluga.api.results.dto.TaskResultDto;
import com.beluga.execution.model.task.TaskExecution;
import com.beluga.model.result.ResultFile;
import com.beluga.result_storage.ResultStorageService;
import org.instancio.Instancio;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import tools.jackson.databind.ObjectMapper;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TaskResultDtoMapperTest {
	
	@Mock
	private ObjectMapper objectMapper;
	@Mock
	private ResultStorageService resultStorageService;
	
	@InjectMocks
	private TaskResultDtoMapper taskResultDtoMapper;
	
	@Test
	void shouldMap() {
		final TaskExecution taskExecution = Instancio.create(TaskExecution.class);
		final ResultFile resultFile = mock();
		when(resultFile.getTaskExecution()).thenReturn(taskExecution);
		when(resultFile.getKey()).thenReturn("data.json");
		when(resultFile.getResourceIdentifier()).thenReturn("resourceIdentifier");
		
		final byte[] rawData = new byte[1];
		when(resultStorageService.retrieve(resultFile.getResourceIdentifier())).thenReturn(rawData);
		
		final List<Object> data = mock();
		when(objectMapper.readValue(rawData, List.class)).thenReturn(data);
		
		final TaskResultDto result = taskResultDtoMapper.map(resultFile);
		
		assertEquals(taskExecution.getId(), result.getTaskId());
		assertSame(data, result.getData());
	}
}