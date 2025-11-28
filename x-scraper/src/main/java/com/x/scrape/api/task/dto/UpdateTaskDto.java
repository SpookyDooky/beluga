package com.x.scrape.api.task.dto;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class UpdateTaskDto {
	
	private List<URL> urls = new ArrayList<>();
	
	public List<URL> getUrls() {
		return urls;
	}
	
	public void setUrls(final List<URL> urls) {
		this.urls = urls;
	}
}
