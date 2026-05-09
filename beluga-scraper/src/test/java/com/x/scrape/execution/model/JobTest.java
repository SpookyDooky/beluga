package com.x.scrape.execution.model;

import com.x.scrape.execution.model.job.Job;
import org.instancio.Instancio;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class JobTest {
	
	@Test
	void shouldGetJobFolder() {
		final Job job = Instancio.create(Job.class);
		
		final String expectedFolder = job.getStorageConfiguration()
				.getFolder() + "/" +
				job.getJobName();
		
		final String actualFolder = job.getJobFolder();
		
		assertEquals(expectedFolder, actualFolder);
	}
	
	@Test
	void shouldGetJobTaskResultFolder() {
		final Job job = Instancio.create(Job.class);
		
		final String expectedFolder = job.getJobFolder()
				+ "/job-executions/" + job.getId() +
				"/results/tasks";
		
		final String actualFolder = job.getJobTaskResultsFolder();
		
		assertEquals(expectedFolder, actualFolder);
	}
}