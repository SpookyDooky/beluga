package com.x.scrape.properties;

import com.x.scrape.properties.scraping.JobProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;

@ConfigurationProperties("x-scraper")
public class XScraperProperties {
	
	private List<JobProperties> jobs;
	
	public List<JobProperties> getJobs() {
		return jobs;
	}
	
	public void setJobs(final List<JobProperties> jobs) {
		this.jobs = jobs;
	}
}
