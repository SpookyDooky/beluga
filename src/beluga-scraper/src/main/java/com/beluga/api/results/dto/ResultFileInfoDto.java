package com.beluga.api.results.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class ResultFileInfoDto {
	
	@NotNull
	private Long id;
	@NotBlank
	private String path;
	@NotBlank
	private String fileName;
	@NotNull
	private Long sizeInBytes;
	
	public Long getId() {
		return id;
	}
	
	public void setId(final Long id) {
		this.id = id;
	}
	
	public String getPath() {
		return path;
	}
	
	public void setPath(final String path) {
		this.path = path;
	}
	
	public String getFileName() {
		return fileName;
	}
	
	public void setFileName(final String fileName) {
		this.fileName = fileName;
	}
	
	public Long getSizeInBytes() {
		return sizeInBytes;
	}
	
	public void setSizeInBytes(final Long sizeInBytes) {
		this.sizeInBytes = sizeInBytes;
	}
}
