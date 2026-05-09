package com.beluga.properties.persistence;

import com.beluga.properties.persistence.validation.HasCorrectPersistenceStore;
import com.beluga.properties.persistence.validation.HasOnlyOnePersistenceStore;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

import static com.beluga.properties.persistence.PersistenceType.SQL_LITE;

/**
 * Used for configuring how the scraper should store things such as jobs, tasks and execution of these things.
 */
@HasCorrectPersistenceStore
@HasOnlyOnePersistenceStore
public class PersistenceProperties {
	
	/**
	 * What type of persistence store to use.
	 * The default is {@link PersistenceType#SQL_LITE}.
	 */
	@NotNull
	private PersistenceType type = SQL_LITE;
	
	@Valid
	private PostgreSqlPersistenceProperties postgresql;
	
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
