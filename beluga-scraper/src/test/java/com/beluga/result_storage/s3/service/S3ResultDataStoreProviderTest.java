package com.beluga.result_storage.s3.service;

import com.beluga.logging.ContextLogger;
import com.beluga.properties.datastore.S3Properties;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.nio.file.Path;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class S3ResultDataStoreProviderTest {
	
	@Mock
	private ContextLogger logger;
	@Mock
	private S3Service s3Service;
	@Mock
	private S3Properties s3Properties;
	
	private final String bucket = "bucket";
	
	private S3ResultDataStoreProvider s3DataStoreProvider;
	
	@BeforeEach
	void setup() {
		when(s3Properties.getBucket()).thenReturn(bucket);
		s3DataStoreProvider = new S3ResultDataStoreProvider(logger, s3Service, s3Properties);
	}
	
	@Test
	void shouldSave() {
		final Path path = mock(RETURNS_DEEP_STUBS);
		final byte[] fileContent = new byte[0];
		
		s3DataStoreProvider.save(path, fileContent);
		
		verify(s3Service).putObject(path, fileContent, bucket);
	}
}