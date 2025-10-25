package com.x.scrape.properties.persistence;

import com.x.scrape.properties.persistence.validation.HasCorrectPersistenceStore;
import com.x.scrape.properties.persistence.validation.HasOnlyOnePersistenceStore;
import jakarta.validation.constraints.NotEmpty;
import org.springframework.boot.context.properties.ConfigurationProperties;

import static com.x.scrape.properties.persistence.PersistenceType.FILE_SYSTEM;

/**
 * Used for configuring how the scraper should store things such as jobs, tasks and execution of these things.
 */
@ConfigurationProperties("x-scraper.persistence")
@HasCorrectPersistenceStore
@HasOnlyOnePersistenceStore
public class PersistenceProperties {
	
	/**
	 * What type of persistence store to use.
	 * The default is {@link PersistenceType#FILE_SYSTEM}.
	 */
	@NotEmpty
	private PersistenceType type = FILE_SYSTEM;
	
	private FileSystemPersistenceProperties fileSystem;
	private S3PersistenceProperties s3;
	
	public PersistenceType getType() {
		return type;
	}
	
	public void setType(final PersistenceType type) {
		this.type = type;
	}
	
	public FileSystemPersistenceProperties getFileSystem() {
		return fileSystem;
	}
	
	public void setFileSystem(final FileSystemPersistenceProperties fileSystem) {
		this.fileSystem = fileSystem;
	}
	
	public S3PersistenceProperties getS3() {
		return s3;
	}
	
	public void setS3(final S3PersistenceProperties s3) {
		this.s3 = s3;
	}
}
