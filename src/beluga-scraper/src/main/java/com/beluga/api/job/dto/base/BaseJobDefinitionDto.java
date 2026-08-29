package com.beluga.api.job.dto.base;

import jakarta.validation.constraints.NotEmpty;
import org.springframework.validation.annotation.Validated;

@Validated
public abstract class BaseJobDefinitionDto {

	@NotEmpty
	private String name;
	
	public String getName() {
		return name;
	}
	
	public void setName(final String name) {
		this.name = name;
	}
}
