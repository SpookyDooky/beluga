package com.x.scrape.api.task.dto;

import java.net.URL;
import java.util.HashSet;
import java.util.Set;

public class UpdateTaskDto {
	
	private Set<URL> urls = new HashSet<>();
	
	public Set<URL> getUrls() {
		return urls;
	}
	
	public void setUrls(final Set<URL> urls) {
		this.urls = urls;
	}
}
