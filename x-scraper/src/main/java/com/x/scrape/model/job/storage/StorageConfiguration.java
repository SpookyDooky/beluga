package com.x.scrape.model.job.storage;

import com.x.scrape.model.types.StorageFormat;

public class StorageConfiguration {
	// job name
		// results
			// job executions
				// tasks
					// json
					// images/pngs
		// configurations
	
	private StorageFormat format;
	
	private String folder;
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
