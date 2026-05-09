package com.beluga.result_storage;

import com.beluga.logging.ContextLogger;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.file.Path;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ResultDataStoreProviderTest {
	
	@Mock
	private ContextLogger logger;
	
	@InjectMocks
	private ResultDataStoreProviderImpl dataStoreProvider;
	
	@BeforeEach
	void setup() {
		dataStoreProvider = spy(dataStoreProvider);
	}
	
	@Test
	void shouldSave() throws Exception {
		final Path path = mock();
		final String fileContent = "string";
		
		try (final InputStream inputStream = new ByteArrayInputStream(fileContent.getBytes())) {
			dataStoreProvider.save(path, inputStream);
			
			verify(dataStoreProvider).save(path, fileContent.getBytes());
		}
	}
	
	static class ResultDataStoreProviderImpl extends ResultDataStoreProvider {
		
		public ResultDataStoreProviderImpl(final ContextLogger logger) {
			super(logger);
		}
		
		@Override
		public void save(final Path filePath, final byte[] fileContent) {
		
		}
		
		@Override
		public byte[] retrieve(final Path filePath) {
			return new byte[0];
		}
	}
}