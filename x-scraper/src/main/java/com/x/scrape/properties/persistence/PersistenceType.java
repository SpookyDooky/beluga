package com.x.scrape.properties.persistence;

/**
 * Used to mark which persistence system should be used for persisting configurations and executions.
 */
public enum PersistenceType {
	FILE_SYSTEM,
	S3,
	POSTGRESQL
}
