package com.beluga.result_storage.s3.service;

import com.beluga.logging.CloseableContext;
import com.beluga.logging.ContextLogger;
import com.beluga.properties.datastore.S3Properties;
import com.beluga.result_storage.ResultDataStoreProvider;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import java.nio.file.Path;

import static com.beluga.logging.ContextKeys.FILE_NAME;

/**
 * Data store provider for S3 compatible object-stores.
 */
@Component
@ConditionalOnProperty(
		name = "beluga.result-datastore.type",
		havingValue = "S3"
)
public class S3ResultDataStoreProvider extends ResultDataStoreProvider {
	
	private final S3Service s3Service;
	private final String bucket;
	
	public S3ResultDataStoreProvider(final ContextLogger logger,
	                                 final S3Service s3Service,
	                                 final S3Properties s3Properties) {
		super(logger);
		this.s3Service = s3Service;
		this.bucket = s3Properties.getBucket();
	}
	
	@Override
	public void save(final Path filePath,
	                 final byte[] fileContent) {
		try (final CloseableContext ignored = logger.with(FILE_NAME, filePath.getFileName().toString())) {
			s3Service.putObject(filePath, fileContent, bucket);
		}
	}
	
	@Override
	public byte[] retrieve(final Path filePath) {
		try (final CloseableContext ignored = logger.with(FILE_NAME, filePath.getFileName().toString())) {
			return s3Service.getObject(filePath, bucket);
		}
	}
}
