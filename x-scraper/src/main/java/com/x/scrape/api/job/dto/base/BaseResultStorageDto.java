package com.x.scrape.api.job.dto.base;

import com.x.scrape.api.job.dto.validation.Path;

public class BaseResultStorageDto {
	
	@Path
	private String folder;
	
	public String getFolder() {
		return folder;
	}
	
	public void setFolder(final String folder) {
		this.folder = folder;
	}
}
