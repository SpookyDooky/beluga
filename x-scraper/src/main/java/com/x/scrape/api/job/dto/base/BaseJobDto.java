package com.x.scrape.api.job.dto.base;

import jakarta.validation.constraints.NotEmpty;

public abstract class BaseJobDto {
	
	@NotEmpty
	private String name;
	
	public String getName() {
		return name;
	}
	
	public void setName(final String name) {
		this.name = name;
	}
}
