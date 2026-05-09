package com.beluga.result_storage.s3.service;

import com.beluga.logging.ContextLogger;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.nio.file.Path;

/**
 * Service that handles communication with S3-compatible object stores.
 */
@Service
@ConditionalOnProperty(
		name = "beluga.result-datastore.type",
		havingValue = "S3"
)
public class S3Service {
	
	private final ContextLogger logger;
	private final S3Client s3Client;
	
	public S3Service(final ContextLogger logger,
	                 @Qualifier("results-s3client") final S3Client s3Client) {
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
				.key(formatKey(filePath))
				.contentType("application/json")
				.build();
		
		final RequestBody requestBody = RequestBody.fromBytes(fileContent);
		s3Client.putObject(putObjectRequest, requestBody);
	}
	
	private String formatKey(final Path filePath) {
		return filePath.toString().replaceAll("\\\\", "/");
	}
	
	/**
	 * Retrieves a document from an S3-compatible object-store.
	 *
	 * @param filePath location of the document.
	 * @param bucket   bucket the document is stored in.
	 * @return returns the document's content as raw bytes.
	 */
	public byte[] getObject(final Path filePath,
	                        final String bucket) {
		final GetObjectRequest getObjectRequest = GetObjectRequest.builder()
				.key(formatKey(filePath))
				.bucket(bucket)
				.build();
		
		final String getObjectResponse = s3Client.getObjectAsBytes(getObjectRequest)
				.toString();
		
		return getObjectResponse.getBytes();
	}
}
