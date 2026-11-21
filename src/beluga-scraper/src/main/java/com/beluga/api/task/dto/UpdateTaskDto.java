package com.beluga.api.task.dto;

import jakarta.validation.constraints.NotNull;
import org.springframework.validation.annotation.Validated;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

@Validated
public class UpdateTaskDto {
	
	private List<@NotNull(message = "A URL must not be null.") URL> urls = new ArrayList<>();
	
	public List<URL> getUrls() {
		return urls;
	}
	
	public void setUrls(final List<URL> urls) {
		this.urls = urls;
	}
}
