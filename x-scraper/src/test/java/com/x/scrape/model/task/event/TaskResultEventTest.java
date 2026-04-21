package com.x.scrape.model.task.event;

import com.x.scrape.execution.model.task.Task;
import com.x.scrape.execution.event.task.task_result.StorageHint;
import com.x.scrape.execution.event.task.task_result.TaskResultEvent;
import com.x.scrape.model.event.storable.payload.Payload;
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
		final StorageHint storageHint = mock();
		final Payload<?> payload = mock();
		
		final TaskResultEvent event = TaskResultEvent.of(task, storageHint, payload);
		
		assertEquals(task.getJob().getId(), event.getJobId());
		assertEquals(task.getId(), event.getTaskId());
		assertSame(storageHint, event.getStorageHint());
		assertSame(payload, event.getPayload());
	}
	
	@Test
	void shouldThrowIllegalArgumentExceptionWhenTaskIsNull() {
		assertThrows(IllegalArgumentException.class, () -> TaskResultEvent.of(null, mock(), mock()));
	}
	
	@Test
	void shouldThrowIllegalArgumentExceptionWhenStorageHintIsNull() {
		assertThrows(IllegalArgumentException.class, () -> TaskResultEvent.of(mock(), null, mock()));
	}
	
	@Test
	void shouldThrowIllegalArgumentExceptionWhenPayloadIsNull() {
		assertThrows(IllegalArgumentException.class, () -> TaskResultEvent.of(mock(), mock(), null));
		
	}
}