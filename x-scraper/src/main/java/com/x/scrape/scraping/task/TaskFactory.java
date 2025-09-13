package com.x.scrape.scraping.task;

import com.x.scrape.model.job.Job;
import com.x.scrape.model.task.Task;
import org.springframework.stereotype.Component;

import java.net.URL;

@Component
public class TaskFactory {
	
	public Task create(final URL url,
	                   final Job job) {
		final Task task = new Task();

		task.setJobId(job.getId());
		task.setUrl(url);
		task.setScrapingConfiguration(job.getScrapingConfiguration());
		
		return task;
	}
}
