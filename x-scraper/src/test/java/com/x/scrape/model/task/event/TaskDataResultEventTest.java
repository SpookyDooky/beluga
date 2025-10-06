package com.x.scrape.model.task.event;

import com.x.scrape.model.task.event.data_result.TaskDataResultEvent;
import com.x.scrape.model.task.event.data_result.data.DataPayload;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

class TaskDataResultEventTest {

	@Test
	void shouldReturnPayload() throws Exception {
		final DataPayload<?> dataPayload = mock();
		
		final TaskDataResultEvent event = new TaskDataResultEvent(
				UUID.randomUUID(),
				UUID.randomUUID(),
				null,
				dataPayload
		);
		
		assertSame(dataPayload, event.getPayload());
	}
	
	@Test
	void shouldThrowNullPointerExceptionIfDataNull() {
		assertThrows(NullPointerException.class, () -> new TaskDataResultEvent(
				UUID.randomUUID(),
				UUID.randomUUID(),
				null,
				null
		));
	}
	
	@Test
	void shouldReturnFileName() {
		final String fileName = "filename";
		
		final TaskDataResultEvent event = new TaskDataResultEvent(
				UUID.randomUUID(),
				UUID.randomUUID(),
				fileName,
				mock()
		);
		
		assertEquals(fileName, event.getFileName().get());
	}
	
	@Test
	void shouldReturnEmptyOptionalWhenRetrievingFileName() {
		final TaskDataResultEvent event = new TaskDataResultEvent(
				UUID.randomUUID(),
				UUID.randomUUID(),
				null,
				mock()
		);
		
		assertTrue(event.getFileName().isEmpty());
	}
}