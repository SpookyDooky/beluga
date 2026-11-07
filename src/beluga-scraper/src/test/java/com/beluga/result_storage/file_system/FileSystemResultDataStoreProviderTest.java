package com.beluga.result_storage.file_system;

import com.beluga.logging.ContextLogger;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.io.TempDir;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(MockitoExtension.class)
class FileSystemResultDataStoreProviderTest {

	private static final byte[] FILE_CONTENT = "content".getBytes();

	@TempDir
	private Path directory;

	@Mock
	private ContextLogger logger;

	@InjectMocks
	private FileSystemResultDataStoreProvider dataStoreProvider;

	@Test
	void shouldSaveAndCreateMissingDirectories() throws IOException {
		final Path file = directory.resolve("job/execution/task/data.json");

		dataStoreProvider.save(file.toString(), FILE_CONTENT);

		assertArrayEquals(FILE_CONTENT, Files.readAllBytes(file));
	}

	@Test
	void shouldSaveInExistingDirectory() throws IOException {
		final Path file = directory.resolve("data.json");

		dataStoreProvider.save(file.toString(), FILE_CONTENT);

		assertArrayEquals(FILE_CONTENT, Files.readAllBytes(file));
	}

	@Test
	void shouldOverwriteExistingFile() throws IOException {
		final Path file = Files.writeString(directory.resolve("data.json"), "a much longer previous content");

		dataStoreProvider.save(file.toString(), FILE_CONTENT);

		assertArrayEquals(FILE_CONTENT, Files.readAllBytes(file));
	}

	@Test
	void shouldThrowWhenSaveFailsWithIOException() throws IOException {
		final Path pathOfExistingDirectory = Files.createDirectory(directory.resolve("data.json"));

		final IllegalStateException exception = assertThrows(
				IllegalStateException.class,
				() -> dataStoreProvider.save(pathOfExistingDirectory.toString(), FILE_CONTENT)
		);

		assertEquals("Could not write file.", exception.getMessage());
		assertInstanceOf(IOException.class, exception.getCause());
	}

	@Test
	void shouldRetrieve() throws IOException {
		final Path file = Files.write(directory.resolve("data.json"), FILE_CONTENT);

		final byte[] result = dataStoreProvider.retrieve(file.toString());

		assertArrayEquals(FILE_CONTENT, result);
	}

	@Test
	void shouldRetrieveWhatWasSaved() {
		final String file = directory.resolve("job/execution/task/data.json").toString();

		dataStoreProvider.save(file, FILE_CONTENT);

		assertArrayEquals(FILE_CONTENT, dataStoreProvider.retrieve(file));
	}

	@Test
	void shouldThrowWhenRetrieveFailsWithIOException() {
		final String missingFile = directory.resolve("missing.json").toString();

		final IllegalStateException exception = assertThrows(
				IllegalStateException.class,
				() -> dataStoreProvider.retrieve(missingFile)
		);

		assertEquals("Could not read file", exception.getMessage());
		assertInstanceOf(IOException.class, exception.getCause());
	}
}
