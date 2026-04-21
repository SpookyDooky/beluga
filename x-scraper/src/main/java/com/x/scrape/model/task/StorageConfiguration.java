package com.x.scrape.model.task;

import com.x.scrape.model.types.StorageFormat;

public class StorageConfiguration {

	private StorageFormat format;
	private String folder;
	
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
