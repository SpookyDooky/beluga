package com.x.scrape.persistence.s3.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.x.scrape.logging.ContextLogger;
import com.x.scrape.persistence.config.conditionals.annotation.IsS3;
import com.x.scrape.properties.persistence.S3PersistenceProperties;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.NoSuchKeyException;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.io.IOException;
import java.nio.file.Path;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicBoolean;

import static java.util.concurrent.TimeUnit.MILLISECONDS;

@Service
@IsS3
public class S3PersistenceService {
	
	private final ContextLogger logger;
	private final S3Client s3Client;
	private final ObjectMapper objectMapper;
	
	// TODO - Maybe the flushing logic should be extracted in an S3 flush service
	private final Map<Path, Object> writeQueue = new ConcurrentHashMap<>();
	private final Map<Path, Instant> lastUpdated = new ConcurrentHashMap<>();
	
	private final String bucket;
	// TODO - This path should be prepended to any received path
	// TODO - Important that none of the supplying services prepend this path otherwise the stored location will be incorrect
	private final String path;
	
	private final AtomicBoolean flushing = new AtomicBoolean(false);
	
	public S3PersistenceService(final ContextLogger logger,
	                            @Qualifier("persistence-s3client") final S3Client s3Client,
	                            final ObjectMapper objectMapper,
	                            final S3PersistenceProperties s3PersistenceProperties) {
		this.logger = logger;
		this.s3Client = s3Client;
		this.objectMapper = objectMapper;
		this.bucket = s3PersistenceProperties.getBucket();
		this.path = s3PersistenceProperties.getFolder();
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
		synchronized (writeQueue) {
			if (writeQueue.containsKey(key)) {
				return Optional.of((T) writeQueue.get(key));
			}
		}
		
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
		return convertKey(key.toString());
	}
	
	private String convertKey(final String key) {
		return key.replaceAll("\\\\", "/");
	}
	
	// TODO - Add debug logs that track the internal queue size which is not being flushed.
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
		synchronized (writeQueue) {
			synchronized (lastUpdated) {
				writeQueue.put(key, object);
				lastUpdated.put(key, Instant.now());
				return object;
			}
		}
	}
	
	// Todo - should use multiple threads to flush items, this should be configurable, necessary for V1.0
	// TODO - make this configurable (V1.0)
	// TODO - Graceful shutdown -> flush to object store before shut down (V1.0)
	@Async
	@Scheduled(fixedRate = 1_000, timeUnit = MILLISECONDS)
	void flushObjects() {
		if (flushing.get()) {
			logger.trace("Write queue size: " + writeQueue.size());
			return;
		}
		flushing.set(true);
		
		logger.info("Flushing S3 persistence write queue.");
		final Instant flushStartTime = Instant.now();
		
		final Map<Path, Object> writeQueueCopy;
		synchronized (writeQueue) {
			writeQueueCopy = new HashMap<>(writeQueue);
		}
		
		writeQueueCopy.forEach((path, value) -> {
			synchronized (writeQueue) {
				synchronized (lastUpdated) {
					if (flushStartTime.isAfter(lastUpdated.get(path))) {
						writeQueue.remove(path);
						lastUpdated.remove(path);
					}
				}
			}
			
			flushObject(path, value);
		});
		
		flushing.set(false);
	}
	
	private void flushObject(final Path key,
	                         final Object object) {
		final String json = toJson(object);
		
		final PutObjectRequest putObjectRequest = PutObjectRequest.builder()
				.bucket(bucket)
				.key(convertKey(key))
				.build();
		final RequestBody requestBody = RequestBody.fromBytes(json.getBytes());
		
		s3Client.putObject(putObjectRequest, requestBody);
	}
	
	private <T> String toJson(final T content) {
		try {
			return objectMapper.writeValueAsString(content);
		} catch (final JsonProcessingException e) {
			throw new IllegalStateException("Could not serialize " + content.getClass().getSimpleName(), e);
		}
		
	}
}
