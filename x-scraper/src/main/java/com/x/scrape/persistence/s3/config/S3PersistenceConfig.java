package com.x.scrape.persistence.s3.config;

import com.x.scrape.persistence.config.conditionals.annotation.IsS3;
import com.x.scrape.properties.persistence.S3PersistenceProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
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
}
