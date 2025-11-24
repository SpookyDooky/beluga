package com.x.scrape.integration_test.properties;

import org.springframework.test.context.DynamicPropertyRegistry;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.postgresql.PostgreSQLContainer;

import java.util.HashMap;
import java.util.Map;

public class TestContainerProperties {
	
	private static final Map<String, GenericContainer<?>> CONTAINERS = new HashMap<>();
	private static String currentProfile;
	
	public static void setCurrentProfile(final String profile,
	                                     final GenericContainer<?> container) {
		currentProfile = profile;
		CONTAINERS.put(profile, container);
		System.setProperty("spring.profiles.active", currentProfile);
	}
	
	public static void overrideProperties(final DynamicPropertyRegistry registry) {
		final GenericContainer<?> container = CONTAINERS.get(currentProfile);
		
		switch (currentProfile) {
			case "postgresql" -> {
				final PostgreSQLContainer postgreSQLContainer = (PostgreSQLContainer) container;
//				System.setProperty("POSTGRE_HOST", postgreSQLContainer.getJdbcUrl());
//				registry.add("x-scraper.persistence.postgresql.url", postgreSQLContainer::getJdbcUrl);
//				registry.add("x-scraper.persistence.type", () -> "POSTGRESQL");
			}
		}
	}
}
