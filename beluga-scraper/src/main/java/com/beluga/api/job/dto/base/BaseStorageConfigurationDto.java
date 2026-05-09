package com.beluga.api.job.dto.base;

import com.beluga.api.job.dto.validation.Path;

public abstract class BaseStorageConfigurationDto {
	
	@Path
	private String folder;
	
	public String getFolder() {
		return folder;
	}
	
	public void setFolder(final String folder) {
		this.folder = folder;
	}
}
