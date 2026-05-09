package com.x.scrape.result_storage.file_system;

import com.x.scrape.logging.ContextLogger;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedConstruction;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.FileOutputStream;
import java.nio.file.Path;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FileSystemResultDataStoreProviderTest {
	
	@Mock
	private ContextLogger logger;
	
	@InjectMocks
	private FileSystemResultDataStoreProvider dataStoreProvider;
	
	@Test
	void shouldSave() throws Exception {
		final Path path = mock(RETURNS_DEEP_STUBS);
		final byte[] fileContent = new byte[0];
		
		try (final MockedConstruction<FileOutputStream> fileOutputStreamMockedConstruction = mockConstruction(FileOutputStream.class)) {
			dataStoreProvider.save(path, fileContent);
			
			verify(path.toFile().getParentFile()).mkdirs();
			
			final FileOutputStream fileOutputStream = fileOutputStreamMockedConstruction.constructed().getFirst();
			verify(fileOutputStream).write(fileContent);
		}
	}
}