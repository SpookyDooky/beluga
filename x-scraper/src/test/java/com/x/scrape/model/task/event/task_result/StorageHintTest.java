package com.x.scrape.model.task.event.task_result;

import org.junit.jupiter.api.Test;

import static com.x.scrape.model.task.event.task_result.StorageType.JSON;
import static org.junit.jupiter.api.Assertions.*;

class StorageHintTest {
	
	@Test
	void shouldCreateStorageHintWithFolderAtRoot() {
		final String fileName = "file";
		final StorageType type = JSON;
		
		final StorageHint result = StorageHint.of(fileName, type);
		
		assertEquals(fileName, result.getFileName());
		assertEquals("/", result.getFolder());
		assertEquals(type, result.getType());
	}
	
	@Test
	void shouldCreateStorageHint() {
		final String fileName = "file";
		final String folder = "folder";
		final StorageType type = JSON;
		
		final StorageHint result = StorageHint.of(fileName, folder, type);
		
		assertEquals(fileName, result.getFileName());
		assertEquals(folder, result.getFolder());
		assertEquals(type, result.getType());
	}
	
	@Test
	void shouldThrowIllegalArgumentExceptionWhenFileNameIsNull() {
		assertThrows(IllegalArgumentException.class, () -> StorageHint.of(null, JSON));
		assertThrows(IllegalArgumentException.class, () -> StorageHint.of(null, "folder", JSON));
	}
	
	@Test
	void shouldThrowIllegalArgumentExceptionWhenFolderIsNull() {
		assertThrows(IllegalArgumentException.class, () -> StorageHint.of("file", null, JSON));
	}
	
	@Test
	void shouldThrowIllegalArgumentExceptionWhenStorageTypeIsNull() {
		assertThrows(IllegalArgumentException.class, () -> StorageHint.of("file", null));
		assertThrows(IllegalArgumentException.class, () -> StorageHint.of("file", "folder", null));
	}
}