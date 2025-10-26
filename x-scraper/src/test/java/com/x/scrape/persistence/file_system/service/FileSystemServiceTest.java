package com.x.scrape.persistence.file_system.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.x.scrape.persistence.shared.service.EntityIdSetterService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class FileSystemServiceTest {

	@Mock
	private ObjectMapper objectMapper;
	@Mock
	private EntityIdSetterService entityIdSetterService;
	
	@InjectMocks
	private FileSystemServiceImpl fileSystemService;
	
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
	
	static class FileSystemServiceImpl extends FileSystemService {
		
		protected FileSystemServiceImpl(final ObjectMapper objectMapper,
		                                final EntityIdSetterService entityIdSetterService) {
			super(objectMapper, entityIdSetterService);
		}
	}
}