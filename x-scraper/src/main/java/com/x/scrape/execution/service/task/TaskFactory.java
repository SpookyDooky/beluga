package com.x.scrape.execution.service.task;

import com.x.scrape.execution.model.Job;
import com.x.scrape.model.task.Task;
import org.springframework.stereotype.Component;

import java.net.URL;

@Component
public class TaskFactory {
	
	public Task create(final URL url,
	                   final Job job) {
		final Task task = new Task();

		task.setJob(job);
		task.setUrl(url);
		task.setScrapingConfiguration(job.getScrapingConfiguration());
		task.setStorageConfiguration(job.getStorageConfiguration());
		
		return task;
	}
}
