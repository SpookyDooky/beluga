package com.x.scrape.result_storage.s3.service;

import com.x.scrape.logging.ContextLogger;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class S3ServiceTest {
	
	@Mock
	private ContextLogger logger;
	@Mock
	private S3Client s3Client;
	
	@InjectMocks
	private S3Service s3Service;
	
	@Captor
	private ArgumentCaptor<PutObjectRequest> putObjectRequestArgumentCaptor;
	
	@Test
	void shouldPutObject() {
		final String path = "some\\path";
		final byte[] fileContent = new byte[1];
		final String bucket = "bucket";
		
		final RequestBody requestBody = mock();
		try (final MockedStatic<RequestBody> requestBodyMockedStatic = Mockito.mockStatic(RequestBody.class)) {
			requestBodyMockedStatic.when(() -> RequestBody.fromBytes(fileContent)).thenReturn(requestBody);
			s3Service.putObject(Path.of(path), fileContent, bucket);
		}
		
		verify(s3Client).putObject(
				putObjectRequestArgumentCaptor.capture(),
				eq(requestBody)
		);
		
		final PutObjectRequest putObjectRequest = putObjectRequestArgumentCaptor.getValue();
		assertEquals(bucket, putObjectRequest.bucket());
		assertEquals("some/path", putObjectRequest.key());
		assertEquals("application/json", putObjectRequest.contentType());
	}
}