package com.x.scrape.execution.service.task;

import com.x.scrape.model.job_definition.JobDefinition;
import com.x.scrape.model.task.Task;
import org.springframework.stereotype.Component;

import java.net.URL;

@Component
public class TaskFactory {
	
	public Task create(final URL url,
	                   final JobDefinition jobDefinition) {
		final Task task = new Task();

		task.setJob(jobDefinition);
		task.setUrl(url);
		task.setScrapingConfiguration(jobDefinition.getJobConfiguration().getScrapingConfiguration());
		task.setStorageConfiguration(jobDefinition.getJobConfiguration().getStorageConfiguration());
		
		return task;
	}
}
