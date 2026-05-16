package com.beluga.mapper;

import com.beluga.execution.event.task.task_result.TaskResultEvent;
import com.beluga.model.result.ResultFile;
import org.instancio.Instancio;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import tools.jackson.databind.ObjectMapper;

import java.lang.reflect.Field;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ResultFileMapperTest {
	
	@Mock
	private ObjectMapper objectMapper;
	
	private final ResultFileMapperImpl mapper = new ResultFileMapperImpl();
	
	@BeforeEach
	void setup() throws Exception {
		final Field objectMapperField = ResultFileMapper.class.getDeclaredField("objectMapper");
		objectMapperField.setAccessible(true);
		
		objectMapperField.set(mapper, objectMapper);
	}
	
	@Test
	void shouldMap() throws Exception {
		final TaskResultEvent taskResultEvent = Instancio.create(TaskResultEvent.class);
		
		final byte[] expectedBytes = new byte[3];
		when(objectMapper.writeValueAsBytes(taskResultEvent.getPayload().getData())).thenReturn(expectedBytes);
		
		final ResultFile resultFile = mapper.map(taskResultEvent);
		
		assertEquals(taskResultEvent.getStorageHint().getPath().toString(), resultFile.getNamespace());
		assertEquals(expectedBytes.length, resultFile.getSizeInBytes());
	}
}