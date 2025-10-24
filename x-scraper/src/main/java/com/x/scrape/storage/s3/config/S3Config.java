package com.x.scrape.storage.s3.config;

import com.x.scrape.properties.datastore.S3Properties;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.services.s3.S3Client;

import java.net.URI;

@Configuration
@ConditionalOnProperty(
		name = "x-scraper.datastore.type",
		havingValue = "S3"
)
public class S3Config {
	
	@Bean
	@ConditionalOnProperty("x-scraper.datastore.s3.host")
	public S3Client s3Client(final S3Properties s3Properties) {
		final AwsBasicCredentials credentials = AwsBasicCredentials.create(
				s3Properties.getAccessKey(), s3Properties.getSecretKey()
		);
		
		return S3Client.builder()
				.endpointOverride(URI.create(s3Properties.getHost()))
				.credentialsProvider(StaticCredentialsProvider.create(credentials))
				.region(s3Properties.getRegion())
				.forcePathStyle(true)
				.build();
	}
}

