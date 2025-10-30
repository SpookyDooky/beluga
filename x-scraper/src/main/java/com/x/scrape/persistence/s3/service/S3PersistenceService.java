package com.x.scrape.persistence.s3.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.x.scrape.persistence.config.conditionals.annotation.IsS3;
import com.x.scrape.persistence.shared.service.EntityIdSetterService;
import com.x.scrape.properties.persistence.S3PersistenceProperties;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.NoSuchKeyException;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Optional;

@Service
@IsS3
public class S3PersistenceService {
	
	private final S3Client s3Client;
	protected final EntityIdSetterService entityIdSetterService;
	private final ObjectMapper objectMapper;
	
	private final String bucket;
	
	public S3PersistenceService(@Qualifier("persistence-s3client") final S3Client s3Client,
	                            final EntityIdSetterService entityIdSetterService,
	                            final ObjectMapper objectMapper,
	                            final S3PersistenceProperties s3PersistenceProperties) {
		this.s3Client = s3Client;
		this.entityIdSetterService = entityIdSetterService;
		this.objectMapper = objectMapper;
		this.bucket = s3PersistenceProperties.getBucket();
	}
	
	/**
	 * Retrieves an object from an S3 compatible object store.
	 *
	 * @param key   the key of the object.
	 * @param clazz the type to change the object into
	 * @param <T>   return type.
	 * @return the object.
	 */
	public <T> Optional<T> getObjectAs(final Path key,
	                                      final Class<T> clazz) {
		final GetObjectRequest getObjectRequest = GetObjectRequest.builder()
				.bucket(bucket)
				.key(convertKey(key))
				.build();
		
		try {
			final byte[] response = s3Client.getObject(getObjectRequest)
					.readAllBytes();
			
			return Optional.ofNullable(objectMapper.readValue(response, clazz));
		} catch (final IOException e) {
			throw new IllegalStateException("Could not get object from S3.", e);
		} catch (final NoSuchKeyException e) {
			return Optional.empty();
		}
	}
	
	private String convertKey(final Path key) {
		return key.toString()
				.replaceAll("\\\\", "/");
	}
	
	/**
	 * Puts an object in the S3 compatible object store.
	 *
	 * @param key    path of the object.
	 * @param object the object to save.
	 * @param <T>    return type.
	 * @return saved object.
	 */
	public <T> T putObject(final Path key,
	                          final T object) {
		entityIdSetterService.setIds(object);
		
		final String json = toJson(object);
		
		final PutObjectRequest putObjectRequest = PutObjectRequest.builder()
				.bucket(bucket)
				.key(convertKey(key))
				.build();
		final RequestBody requestBody = RequestBody.fromBytes(json.getBytes());
		
		s3Client.putObject(putObjectRequest, requestBody);
		
		return object;
	}
	
	private <T> String toJson(final T content) {
		try {
			return objectMapper.writeValueAsString(content);
		} catch (final JsonProcessingException e) {
			throw new IllegalStateException("Could not serialize " + content.getClass().getSimpleName(), e);
		}
	}
}
