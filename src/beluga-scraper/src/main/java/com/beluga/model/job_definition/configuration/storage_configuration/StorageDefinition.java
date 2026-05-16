package com.beluga.model.job_definition.configuration.storage_configuration;

import com.beluga.model.types.StorageFormat;
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
}
