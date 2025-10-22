package com.x.scrape.storage.s3.service;

import com.x.scrape.logging.ContextLogger;
import com.x.scrape.properties.datastore.S3Properties;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class S3DataStoreProviderTest {
	
	@Mock
	private ContextLogger logger;
	@Mock
	private S3Service s3Service;
	@Mock
	private S3Properties s3Properties;
	
	private final String bucket = "bucket";
	
	private S3DataStoreProvider s3DataStoreProvider;
	
	@BeforeEach
	void setup() {
		when(s3Properties.getBucket()).thenReturn(bucket);
		s3DataStoreProvider = new S3DataStoreProvider(logger, s3Service, s3Properties);
	}
	
	@Test
	void shouldSaveStream() {
		Assertions.fail();
	}
	
	@Test
	void shouldSave() {
		Assertions.fail();
	}
}