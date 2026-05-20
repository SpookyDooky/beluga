package com.beluga.properties.persistence;

import com.beluga.properties.persistence.validation.HasCorrectPersistenceStore;
import com.beluga.properties.persistence.validation.HasOnlyOnePersistenceStore;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

import static com.beluga.properties.persistence.PersistenceType.SQLITE;

/**
 * Used for configuring how the scraper should store things such as jobs, tasks and execution of these things.
 */
@HasCorrectPersistenceStore
@HasOnlyOnePersistenceStore
public class PersistenceProperties {
	
	/**
	 * What type of persistence store to use.
	 * The default is {@link PersistenceType#SQLITE}.
	 */
	@NotNull
	private PersistenceType type = SQLITE;

	/**
	 * PostgreSQL properties.
	 */
	@Valid
	private PostgreSqlPersistenceProperties postgresql;

	/**
	 * Returns the type of persistence used.
	 * @return {@link PersistenceType}
	 */
	public PersistenceType getType() {
		return type;
	}
	
	public void setType(final PersistenceType type) {
		this.type = type;
	}
	
	public PostgreSqlPersistenceProperties getPostgresql() {
		return postgresql;
	}
	
	public void setPostgresql(final PostgreSqlPersistenceProperties postgresql) {
		this.postgresql = postgresql;
	}
}
