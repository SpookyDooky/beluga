package com.x.scrape.result_storage.s3.service;

import com.x.scrape.logging.ContextLogger;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.nio.file.Path;

/**
 * Service that handles communication with S3-compatible object stores.
 */
@Service
@ConditionalOnBean(S3Client.class)
public class S3Service {
	
	private final ContextLogger logger;
	private final S3Client s3Client;
	
	public S3Service(final ContextLogger logger,
	                 final S3Client s3Client) {
		this.logger = logger;
		this.s3Client = s3Client;
	}
	
	/**
	 * Upload a document to S3-compatible object-store.
	 *
	 * @param filePath    location of the document.
	 * @param fileContent contents of the document.
	 * @param bucket      location to store it in.
	 */
	public void putObject(final Path filePath,
	                      final byte[] fileContent,
	                      final String bucket) {
		logger.info("Uploading file to S3.");
		final PutObjectRequest putObjectRequest = PutObjectRequest.builder()
				.bucket(bucket)
				.key(filePath.toString().replaceAll("\\\\", "/"))
				.contentType("application/json")
				.build();
		
		final RequestBody requestBody = RequestBody.fromBytes(fileContent);
		s3Client.putObject(putObjectRequest, requestBody);
	}
}
