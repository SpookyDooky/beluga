package com.x.scrape.integration_test;

import org.junit.jupiter.api.BeforeAll;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.postgresql.PostgreSQLContainer;

@SpringBootTest
@ActiveProfiles("postgresql")
public class BasePostgreIntegrationTest {
	
	@Container
	static PostgreSQLContainer postgreSQLContainer = new PostgreSQLContainer("postgres:17.0")
			.withDatabaseName("test")
			.withUsername("username")
			.withPassword("password");
	
	@BeforeAll
	static void setup() {
		postgreSQLContainer.start();
		System.setProperty("POSTGRE_HOST", postgreSQLContainer.getJdbcUrl());
	}
}
