package com.beluga.result_storage.s3.service;

import com.beluga.logging.config.LoggingConfig;
import com.beluga.properties.BelugaScraperProperties;
import com.beluga.result_storage.s3.config.S3Config;
import org.instancio.Instancio;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.testcontainers.containers.MinIOContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;
import software.amazon.awssdk.services.s3.S3Client;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;

/**
 * Stores and retrieves a result through the real S3 beans against MinIO. Only the S3 beans are loaded, no persistence.
 */
@SpringBootTest(classes = {
		S3ResultDataStoreProviderIntegrationTest.TestConfig.class,
		LoggingConfig.class,
		S3Config.class,
		S3Service.class,
		S3ResultDataStoreProvider.class
})
@ActiveProfiles("s3")
@Testcontainers
class S3ResultDataStoreProviderIntegrationTest {
	
	private static final DockerImageName MINIO_IMAGE = DockerImageName.parse("docker.io/pgsty/silo:9.9")
			.asCompatibleSubstituteFor("minio/minio");
	private static final String BUCKET = "results";

	@Container
	private static final MinIOContainer MINIO = new MinIOContainer(MINIO_IMAGE)
			.withUserName("username")
			.withPassword("password");
	
	static {
		MINIO.start();
		System.setProperty("MINIO_HOST", MINIO.getS3URL());
	}
	
	@Autowired
	private S3ResultDataStoreProvider s3ResultDataStoreProvider;
	@Autowired
	private S3Client s3Client;
	
	@Test
	void shouldRetrieveSavedResult() {
		s3Client.createBucket(builder -> builder.bucket(BUCKET));
		
		final String resourceIdentifier = "/1/2/3/data.json";
		final byte[] fileContent = Instancio.create(String.class).getBytes();
		
		s3ResultDataStoreProvider.save(resourceIdentifier, fileContent);
		final byte[] result = s3ResultDataStoreProvider.retrieve(resourceIdentifier);
		
		assertArrayEquals(fileContent, result);
	}
	
	@EnableConfigurationProperties(BelugaScraperProperties.class)
	static class TestConfig {
	
	}
}
