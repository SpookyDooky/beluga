package com.beluga.model.task.event;

import com.beluga.execution.model.task.Task;
import com.beluga.execution.event.task.task_result.TaskResultEvent;
import com.beluga.model.event.storable.payload.Payload;
import org.instancio.Instancio;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

@ExtendWith(MockitoExtension.class)
class TaskResultEventTest {
	
	@Test
	void shouldCreateTaskResultEvent() {
		final Task task = Instancio.create(Task.class);
		final String fileName = "fileName";
		final Payload<?> payload = mock();
		
		final TaskResultEvent event = TaskResultEvent.of(task, fileName, payload);

		assertEquals(task.getJob().getJobDefinitionId(), event.getJobDefinitionId());
		assertEquals(task.getJob().getId(), event.getJobId());
		assertEquals(task.getId(), event.getTaskId());
		assertEquals(fileName, event.getKey());
		assertSame(payload, event.getPayload());
	}
	
	@Test
	void shouldThrowIllegalArgumentExceptionWhenTaskIsNull() {
		assertThrows(IllegalArgumentException.class, () -> TaskResultEvent.of(null, "key", mock()));
	}
	
	@Test
	void shouldThrowIllegalArgumentExceptionWhenStorageHintIsNull() {
		assertThrows(IllegalArgumentException.class, () -> TaskResultEvent.of(mock(), null, mock()));
	}
	
	@Test
	void shouldThrowIllegalArgumentExceptionWhenPayloadIsNull() {
		assertThrows(IllegalArgumentException.class, () -> TaskResultEvent.of(mock(), "key", null));
	}
}