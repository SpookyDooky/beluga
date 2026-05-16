package com.beluga.result_storage.s3.service;

import com.beluga.logging.CloseableContext;
import com.beluga.logging.ContextLogger;
import com.beluga.properties.datastore.S3Properties;
import com.beluga.result_storage.ResultDataStoreProvider;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import static com.beluga.logging.ContextKeys.RESOURCE_IDENTIFIER;

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
	public void save(final String resourceIdentifier,
	                 final byte[] fileContent) {
		try (final CloseableContext ignored = logger.with(RESOURCE_IDENTIFIER, resourceIdentifier)) {
			s3Service.putObject(
					resourceIdentifier,
					fileContent,
					bucket
			);
		}
	}
	
	@Override
	public byte[] retrieve(final String resourceIdentifier) {
		try (final CloseableContext ignored = logger.with(RESOURCE_IDENTIFIER, resourceIdentifier)) {
			return s3Service.getObject(resourceIdentifier, bucket);
		}
	}
}
