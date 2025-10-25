package com.x.scrape.properties.persistence;

import jakarta.validation.constraints.NotEmpty;

/**
 * Configures persistence on a file system.
 */
public class FileSystemPersistenceProperties {
	
	/**
	 * The folder that is used to store all persistent data in.
	 */
	@NotEmpty
	private String folder;
	
	public String getFolder() {
		return folder;
	}
	
	public void setFolder(final String folder) {
		if (folder.endsWith("/")) {
			this.folder = folder.substring(0, folder.length() - 1);
		} else {
			this.folder = folder;
		}
	}
}
