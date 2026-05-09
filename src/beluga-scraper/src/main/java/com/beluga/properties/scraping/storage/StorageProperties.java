package com.beluga.properties.scraping.storage;

import com.beluga.model.types.StorageFormat;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class StorageProperties {
	
	@NotNull
	private StorageFormat format;
	
	@NotBlank
	private String folder;
	@NotBlank
	private String file;
	
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
	
	public String getFile() {
		return file;
	}
	
	public void setFile(final String file) {
		this.file = file;
	}
}
