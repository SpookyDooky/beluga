package com.beluga.execution.event.task.task_result;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class StorageHintTest {
	
	@Test
	void shouldCreateStorageHintWithFolderAtRoot() {
		final String fileName = "file";
		
		final StorageHint result = StorageHint.of(fileName);
		
		assertEquals(fileName, result.getFileName());
		assertEquals("/", result.getFolder());
	}
	
	@Test
	void shouldCreateStorageHint() {
		final String fileName = "file";
		final String folder = "folder";
		
		final StorageHint result = StorageHint.of(fileName, folder);
		
		assertEquals(fileName, result.getFileName());
		assertEquals(folder, result.getFolder());
	}
	
	@Test
	void shouldThrowIllegalArgumentExceptionWhenFileNameIsNull() {
		assertThrows(IllegalArgumentException.class, () -> StorageHint.of(null));
		assertThrows(IllegalArgumentException.class, () -> StorageHint.of(null, "folder"));
	}
	
	@Test
	void shouldThrowIllegalArgumentExceptionWhenFolderIsNull() {
		assertThrows(IllegalArgumentException.class, () -> StorageHint.of("file", null));
	}
}