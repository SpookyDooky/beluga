package com.x.scrape.properties.persistence;

import com.x.scrape.properties.persistence.validation.HasCorrectPersistenceStore;
import com.x.scrape.properties.persistence.validation.HasOnlyOnePersistenceStore;
import jakarta.validation.constraints.NotEmpty;
import org.springframework.boot.context.properties.ConfigurationProperties;

import static com.x.scrape.properties.persistence.PersistenceType.SQL_LITE;

/**
 * Used for configuring how the scraper should store things such as jobs, tasks and execution of these things.
 */
@ConfigurationProperties("x-scraper.persistence")
@HasCorrectPersistenceStore
@HasOnlyOnePersistenceStore
public class PersistenceProperties {
	
	/**
	 * What type of persistence store to use.
	 * The default is {@link PersistenceType#SQL_LITE}.
	 */
	@NotEmpty
	private PersistenceType type = SQL_LITE;
	
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
