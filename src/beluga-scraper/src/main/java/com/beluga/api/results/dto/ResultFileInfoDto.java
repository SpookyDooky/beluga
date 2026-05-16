package com.beluga.api.results.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class ResultFileInfoDto {
	
	@NotNull
	private Long id;
	@NotBlank
	private String resourceIdentifier;
	@NotNull
	private Long sizeInBytes;
	
	public Long getId() {
		return id;
	}
	
	public void setId(final Long id) {
		this.id = id;
	}

	public String getResourceIdentifier() {
		return resourceIdentifier;
	}

	public void setResourceIdentifier(String resourceIdentifier) {
		this.resourceIdentifier = resourceIdentifier;
	}

	public Long getSizeInBytes() {
		return sizeInBytes;
	}
	
	public void setSizeInBytes(final Long sizeInBytes) {
		this.sizeInBytes = sizeInBytes;
	}
}
