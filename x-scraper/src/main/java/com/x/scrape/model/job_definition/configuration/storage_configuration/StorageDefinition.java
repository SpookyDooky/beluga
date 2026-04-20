package com.x.scrape.model.job_definition.configuration.storage_configuration;

import com.x.scrape.model.types.StorageFormat;
import jakarta.persistence.*;

import static jakarta.persistence.EnumType.STRING;
import static jakarta.persistence.GenerationType.IDENTITY;

@Entity
public class StorageDefinition {
	
	@Id
	@GeneratedValue(strategy = IDENTITY)
	private Long id;
	
	// Todo - remove as this is not used for anything Only json will be offered as result storage format
	@Enumerated(STRING)
	private StorageFormat format;
	
	// TODO - This should never be able to change once it has been set
	private String folder;
	
	public Long getId() {
		return id;
	}
	
	public void setId(final Long id) {
		this.id = id;
	}
	
	public StorageFormat getFormat() {
		return format;
	}
	
	public void setFormat(final StorageFormat format) {
		this.format = format;
	}
	
	public String getFolder() {
		return folder;
	}
	
	public void setFolder(final String folder) {
		this.folder = folder;
	}
}
