package com.beluga.execution.model.task;

import com.beluga.execution.model.job.Job;
import com.beluga.logging.ContextLoggable;

import java.net.URL;
import java.util.Map;

import static com.beluga.logging.ContextKeys.TASK_ID;
import static com.beluga.logging.ContextKeys.URL;

public class Task implements ContextLoggable {
	
	private Long id;
	private Job job;
	
	private URL url;
	private ScrapingConfiguration scrapingConfiguration;
	
	public Long getId() {
		return id;
	}
	
	public void setId(final Long id) {
		this.id = id;
	}
	
	public Job getJob() {
		return job;
	}
	
	public void setJob(final Job job) {
		this.job = job;
	}
	
	public URL getUrl() {
		return url;
	}
	
	public void setUrl(final URL url) {
		this.url = url;
	}
	
	public ScrapingConfiguration getScrapingConfiguration() {
		return scrapingConfiguration;
	}
	
	public void setScrapingConfiguration(final ScrapingConfiguration scrapingConfiguration) {
		this.scrapingConfiguration = scrapingConfiguration;
	}
	
	@Override
	public Map<String, String> loggingContext() {
		return Map.of(
				TASK_ID, id.toString(),
				URL, url.toString()
		);
	}
}
