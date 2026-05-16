package com.beluga.result_storage.s3.service;

import com.beluga.logging.ContextLogger;
import com.beluga.properties.BelugaScraperProperties;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Answers.RETURNS_DEEP_STUBS;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class S3ResultDataStoreProviderTest {
	
	@Mock
	private ContextLogger logger;
	@Mock
	private S3Service s3Service;
	@Mock(answer = RETURNS_DEEP_STUBS)
	private BelugaScraperProperties belugaScraperProperties;
	
	private final String bucket = "bucket";
	
	private S3ResultDataStoreProvider s3DataStoreProvider;
	
	@BeforeEach
	void setup() {
		when(belugaScraperProperties.getResultStorage().getS3().getBucket()).thenReturn(bucket);
		s3DataStoreProvider = new S3ResultDataStoreProvider(logger, s3Service, belugaScraperProperties);
	}
	
	@Test
	void shouldSave() {
		final String path = "path";
		final byte[] fileContent = new byte[0];
		
		s3DataStoreProvider.save(path, fileContent);
		
		verify(s3Service).putObject(path, fileContent, bucket);
	}
}