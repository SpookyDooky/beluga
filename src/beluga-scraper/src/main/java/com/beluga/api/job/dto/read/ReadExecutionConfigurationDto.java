package com.beluga.api.job.dto.read;

import com.beluga.api.job.dto.base.BaseExecutionConfigurationDto;

public class ReadExecutionConfigurationDto extends BaseExecutionConfigurationDto {
	
	private Long id;
	
	public Long getId() {
		return id;
	}
	
	public void setId(final Long id) {
		this.id = id;
	}
}
