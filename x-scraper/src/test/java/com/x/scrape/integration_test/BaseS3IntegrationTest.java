package com.x.scrape.integration_test;

import org.junit.jupiter.api.BeforeAll;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.testcontainers.containers.MinIOContainer;
import org.testcontainers.junit.jupiter.Container;

@SpringBootTest
@ActiveProfiles("s3")
public class BaseS3IntegrationTest {
	
	@Container
	static MinIOContainer minIo = new MinIOContainer("minio/minio:latest")
			.withUserName("username")
			.withPassword("password");
	
	@BeforeAll
	static void setup() {
		minIo.start();
		System.setProperty("S3_HOST", minIo.getS3URL());
	}
}
