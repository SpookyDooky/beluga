package com.beluga.api.task.dto;

import jakarta.validation.constraints.NotNull;
import org.springframework.validation.annotation.Validated;

import java.net.URL;
import java.util.HashSet;
import java.util.Set;

@Validated
public class UpdateTaskDto {
	
	private Set<@NotNull(message = "A URL must not be null.") URL> urls = new HashSet<>();
	
	public Set<URL> getUrls() {
		return urls;
	}
	
	public void setUrls(final Set<URL> urls) {
		this.urls = urls;
	}
}
