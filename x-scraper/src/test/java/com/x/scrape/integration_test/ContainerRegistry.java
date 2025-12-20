package com.x.scrape.integration_test;

import com.x.scrape.properties.persistence.PersistenceType;
import org.testcontainers.containers.GenericContainer;

import java.util.HashMap;
import java.util.Map;

public class ContainerRegistry {
	
	private static Map<PersistenceType, GenericContainer<?>> runningContainers = new HashMap<>();
	
	public static void registerContainerAsRunning(final PersistenceType persistenceType,
	                                              final GenericContainer<?> container) {
		runningContainers.put(persistenceType, container);
	}
	
	public static boolean isContainerRunning(final PersistenceType persistenceType) {
		return runningContainers.containsKey(persistenceType);
	}
}
