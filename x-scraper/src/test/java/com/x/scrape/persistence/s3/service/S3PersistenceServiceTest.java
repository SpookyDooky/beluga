package com.x.scrape.persistence.s3.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.x.scrape.logging.ContextLogger;
import com.x.scrape.properties.persistence.S3PersistenceProperties;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import software.amazon.awssdk.core.ResponseInputStream;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.GetObjectResponse;
import software.amazon.awssdk.services.s3.model.NoSuchKeyException;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.nio.file.Path;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class S3PersistenceServiceTest {
	
	private static final String BUCKET = "bucket";
	
	@Mock
	private ContextLogger logger;
	@Mock
	private S3Client s3Client;
	@Mock
	private ObjectMapper objectMapper;
	@Mock
	private S3PersistenceProperties s3PersistenceProperties;
	
	private S3PersistenceService s3PersistenceService;
	
	@Captor
	private ArgumentCaptor<GetObjectRequest> getObjectRequestArgumentCaptor;
	@Captor
	private ArgumentCaptor<PutObjectRequest> putObjectRequestArgumentCaptor;
	@Captor
	private ArgumentCaptor<RequestBody> requestBodyArgumentCaptor;
	
	@BeforeEach
	void setup() {
		when(s3PersistenceProperties.getBucket()).thenReturn(BUCKET);
		s3PersistenceService = new S3PersistenceService(
				logger,
				s3Client,
				objectMapper,
				s3PersistenceProperties
		);
	}
	
	@Test
	void shouldGetObjectAs() throws Exception {
		final Path key = Path.of("some\\key");
		final Class<String> returnType = String.class;
		
		final ResponseInputStream<GetObjectResponse> response = mock();
		final byte[] responseBytes = new byte[0];
		when(response.readAllBytes()).thenReturn(responseBytes);
		when(s3Client.getObject(getObjectRequestArgumentCaptor.capture())).thenReturn(response);
		
		final String responseBody = "test";
		when(objectMapper.readValue(responseBytes, returnType)).thenReturn(responseBody);
		
		final Optional<String> result = s3PersistenceService.getObjectAs(key, returnType);
		
		assertEquals(responseBody, result.get());
		
		final GetObjectRequest getObjectRequest = getObjectRequestArgumentCaptor.getValue();
		assertEquals(BUCKET, getObjectRequest.bucket());
		assertEquals("some/key", getObjectRequest.key());
	}
	
	@Test
	void shouldGetObjectAsEmpty() {
		when(s3Client.getObject(any(GetObjectRequest.class))).thenThrow(NoSuchKeyException.class);
		
		final Optional<String> result = s3PersistenceService.getObjectAs(Path.of("test"), String.class);
		
		assertTrue(result.isEmpty());
	}
	
	@Test
	void shouldGetObjectFromWriteQueue() {
		final Path key = Path.of("some\\key");
		final String object = "object";
		
		s3PersistenceService.putObject(key, object);
		verifyNoInteractions(s3Client);
		
		final String result = s3PersistenceService.getObjectAs(key, String.class)
				.get();
		verifyNoInteractions(s3Client);
		
		assertSame(object, result);
	}
	
	@Test
	void shouldPutObject() throws Exception {
		final Path key = Path.of("some\\key");
		final String object = "object";
		
		final String json = "json";
		when(objectMapper.writeValueAsString(object)).thenReturn(json);
		
		when(s3Client.putObject(
				putObjectRequestArgumentCaptor.capture(),
				requestBodyArgumentCaptor.capture()
		)).thenReturn(mock());
		
		final String result = s3PersistenceService.putObject(key, object);
		s3PersistenceService.flushObjects();
		
		assertSame(object, result);
		
		final PutObjectRequest putObjectRequest = putObjectRequestArgumentCaptor.getValue();
		assertEquals(BUCKET, putObjectRequest.bucket());
		assertEquals("some/key", putObjectRequest.key());
		
		final RequestBody requestBody = requestBodyArgumentCaptor.getValue();
		assertEquals(json.getBytes().length, requestBody.optionalContentLength().get().intValue());
	}
}