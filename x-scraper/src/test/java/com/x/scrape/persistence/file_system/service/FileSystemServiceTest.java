package com.x.scrape.persistence.file_system.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FileSystemServiceTest {

	@Mock
	private ObjectMapper objectMapper;
	
	@InjectMocks
	private FileSystemService fileSystemService;
	
	@Test
	void shouldGet() {
		final String filePath = FileSystemServiceTest.class
				.getResource("/test-files/dummy-file.txt")
				.getPath()
				.replaceFirst("/", "");
		
		final Optional<File> fileOptional = fileSystemService.get(Path.of(filePath));
		
		assertTrue(fileOptional.isPresent());
	}
	
	@Test
	void shouldGetEmpty() {
		assertTrue(fileSystemService.get(Path.of("some/path")).isEmpty());
	}
	
	@Test
	void shouldReadFileAs() throws Exception {
		final String filePath = FileSystemServiceTest.class
				.getResource("/test-files/dummy-file.txt")
				.getPath()
				.replaceFirst("/", "");
		final File file = new File(filePath);
		
		final String expectedResult = "test";
		when(objectMapper.readValue("dummy-file", String.class)).thenReturn(expectedResult);
		
		final String result = fileSystemService.readFileAs(file, String.class);
		
		assertEquals(expectedResult, result);
	}
	
	@Test
	void shouldSave() throws Exception {
		final String filePath = FileSystemServiceTest.class
				.getResource("/test-files/dummy-file.txt")
				.getPath()
				.replaceFirst("/", "");
		final Path path = Path.of(filePath);
		
		final String fileContent = "dummy-file";
		when(objectMapper.writeValueAsString(fileContent)).thenReturn(fileContent);
		
		final String result = fileSystemService.save(fileContent, path);
		
		assertEquals(fileContent, result);
		final String actualContent = Files.readString(path);
		assertEquals(fileContent, actualContent);
	}
	
	@Test
	void shouldSaveNewFile() throws Exception {
		final String filePath = FileSystemServiceTest.class
				.getResource("/test-files/dummy-file.txt")
				.getPath()
				.replaceFirst("/", "")
				.replace("dummy-file.txt", "dummy-file2.txt");
		final Path path = Path.of(filePath);
		
		final String fileContent = "dummy-file";
		when(objectMapper.writeValueAsString(fileContent)).thenReturn(fileContent);
		
		final File file = new File(filePath);
		assertFalse(file.exists());
		
		final String result = fileSystemService.save(fileContent, path);
		
		assertEquals(fileContent, result);
		final String actualContent = Files.readString(path);
		assertEquals(fileContent, actualContent);
		
		assertTrue(file.exists());
		file.delete();
	}
	
	@Test
	void shouldListFiles() {
		final Path path = mock(RETURNS_DEEP_STUBS);
		final File file = mock();
		when(path.toFile().listFiles()).thenReturn(new File[]{file});
		
		final List<File> files = fileSystemService.listFiles(path);
		
		assertEquals(1, files.size());
		assertTrue(files.contains(file));
	}
	
	@Test
	void shouldReturnEmptyListForListFiles() {
		final Path path = mock(RETURNS_DEEP_STUBS);
		when(path.toFile().listFiles()).thenReturn(new File[]{});
		
		final List<File> files = fileSystemService.listFiles(path);
		
		assertTrue(files.isEmpty());
	}
}