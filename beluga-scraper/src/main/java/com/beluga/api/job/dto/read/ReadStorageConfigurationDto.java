package com.beluga.api.job.dto.read;

import com.beluga.api.job.dto.base.BaseStorageConfigurationDto;

public class ReadStorageConfigurationDto extends BaseStorageConfigurationDto  {
	
	private Long id;
	
	public Long getId() {
		return id;
	}
	
	public void setId(final Long id) {
		this.id = id;
	}
}
