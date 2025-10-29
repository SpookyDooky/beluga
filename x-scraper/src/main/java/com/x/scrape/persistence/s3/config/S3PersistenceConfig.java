package com.x.scrape.persistence.s3.config;

import com.x.scrape.persistence.config.conditionals.annotation.IsS3;
import com.x.scrape.persistence.s3.service.S3StateService;
import com.x.scrape.persistence.shared.service.PersistenceIdService;
import com.x.scrape.properties.persistence.S3PersistenceProperties;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionException;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.SimpleTransactionStatus;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.services.s3.S3Client;

import java.net.URI;

@IsS3
@Configuration
public class S3PersistenceConfig {
	
	@Bean("persistence-s3client")
	public S3Client s3Client(final S3PersistenceProperties properties) {
		final AwsBasicCredentials credentials = AwsBasicCredentials.create(
				properties.getAccessKey(), properties.getSecretKey()
		);
		
		return S3Client.builder()
				.endpointOverride(URI.create(properties.getHost()))
				.credentialsProvider(StaticCredentialsProvider.create(credentials))
				.region(properties.getRegion())
				.forcePathStyle(true)
				.build();
	}
	
	@Bean
	public PersistenceIdService persistenceIdService(final S3StateService s3StateService,
	                                                 final ApplicationEventPublisher applicationEventPublisher) {
		final Long sequence = s3StateService.getSequence();
		return new PersistenceIdService(sequence, applicationEventPublisher);
	}
	
	@Bean
	public PlatformTransactionManager noOperationTransactionManager() {
		return new PlatformTransactionManager() {
			@Override
			public TransactionStatus getTransaction(final TransactionDefinition definition) throws TransactionException {
				return new SimpleTransactionStatus();
			}
			
			@Override
			public void commit(final TransactionStatus status) throws TransactionException {
			
			}
			
			@Override
			public void rollback(final TransactionStatus status) throws TransactionException {
			
			}
		};
	}
}
