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

import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;

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
		final String resourceIdentifier = "resourceIdentifier";
		final String fileContent = "string";
		
		try (final InputStream inputStream = new ByteArrayInputStream(fileContent.getBytes())) {
			dataStoreProvider.save(resourceIdentifier, inputStream);
			
			verify(dataStoreProvider).save(resourceIdentifier, fileContent.getBytes());
		}
	}
	
	static class ResultDataStoreProviderImpl extends ResultDataStoreProvider {
		
		public ResultDataStoreProviderImpl(final ContextLogger logger) {
			super(logger);
		}
		
		@Override
		public void save(final String resourceIdentifier, final byte[] fileContent) {
		
		}
		
		@Override
		public byte[] retrieve(final String resourceIdentifier) {
			return new byte[0];
		}
	}
}