package com.x.scrape.storage.s3.service;

import com.x.scrape.logging.CloseableContext;
import com.x.scrape.logging.ContextLogger;
import com.x.scrape.properties.datastore.S3Properties;
import com.x.scrape.storage.DataStoreProvider;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.stereotype.Component;

import java.nio.file.Path;

import static com.x.scrape.logging.ContextKeys.FILE_NAME;

@Component
@ConditionalOnBean(S3Service.class)
public class S3DataStoreProvider extends DataStoreProvider {
	
	private final S3Service s3Service;
	private final String bucket;
	
	public S3DataStoreProvider(final ContextLogger logger,
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
}
