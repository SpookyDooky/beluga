package com.x.scrape.api.task.dto;

import java.net.URL;

public class ReadTaskDefinitionDto {
	
	private Long id;
	private URL url;
	
	public Long getId() {
		return id;
	}
	
	public void setId(final Long id) {
		this.id = id;
	}
	
	public URL getUrl() {
		return url;
	}
	
	public void setUrl(final URL url) {
		this.url = url;
	}
}
