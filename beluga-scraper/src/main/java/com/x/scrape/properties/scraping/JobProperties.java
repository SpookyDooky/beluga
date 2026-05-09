package com.x.scrape.properties.scraping;

import com.x.scrape.properties.scraping.execution.ExecutionProperties;
import com.x.scrape.properties.scraping.storage.StorageProperties;
import com.x.scrape.properties.scraping.url.UrlProperties;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class JobProperties {
	
	@NotBlank
	private String name;
	private UrlProperties url;
	
	@NotNull
	@Valid
	private ScrapingProperties scraping;
	@NotNull
	@Valid
	private StorageProperties storage;
	@NotNull
	@Valid
	private ExecutionProperties execution = new ExecutionProperties();
	
	public String getName() {
		return name;
	}
	
	public void setName(final String name) {
		this.name = name;
	}
	
	public UrlProperties getUrl() {
		return url;
	}
	
	public void setUrl(final UrlProperties url) {
		this.url = url;
	}
	
	public ScrapingProperties getScraping() {
		return scraping;
	}
	
	public void setScraping(final ScrapingProperties scraping) {
		this.scraping = scraping;
	}
	
	public StorageProperties getStorage() {
		return storage;
	}
	
	public void setStorage(final StorageProperties storage) {
		this.storage = storage;
	}
	
	public ExecutionProperties getExecution() {
		return execution;
	}
	
	public void setExecution(final ExecutionProperties execution) {
		this.execution = execution;
	}
}
