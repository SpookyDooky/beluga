package com.x.scrape.model.task.event;

import org.junit.jupiter.api.Test;

import java.io.InputStream;
import java.io.ObjectInputStream;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class TaskDataResultEventTest {

	@Test
	void shouldTurnObjectIntoInputStream() throws Exception {
		final String data = "data";
		
		final TaskDataResultEvent event = new TaskDataResultEvent(
				UUID.randomUUID(),
				UUID.randomUUID(),
				data
		);
		
		final InputStream dataInputStream = event.getData();
		final ObjectInputStream objectInputStream = new ObjectInputStream(dataInputStream);
		final String reconstructedData = (String) objectInputStream.readObject();
		
		assertEquals(data, reconstructedData);
	}
	
	@Test
	void shouldThrowNullPointerExceptionIfDataNull() {
		assertThrows(NullPointerException.class, () -> new TaskDataResultEvent(
				UUID.randomUUID(),
				UUID.randomUUID(),
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
				"data"
		);
		
		assertEquals(fileName, event.getFileName().get());
	}
	
	@Test
	void shouldReturnEmptyOptionalWhenRetrievingFileName() {
		final TaskDataResultEvent event = new TaskDataResultEvent(
				UUID.randomUUID(),
				UUID.randomUUID(),
				"data"
		);
		
		assertTrue(event.getFileName().isEmpty());
	}
}