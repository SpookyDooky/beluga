package com.x.scrape.model.job;

import org.instancio.Instancio;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.mockito.Mockito.mock;

class JobTest {
	
	@Test
	void shouldCreateUniqueId() {
		final UUID id1 = new Job(mock()).getId();
		final UUID id2 = new Job(mock()).getId();
		
		assertNotEquals(id1, id2);
	}
	
	@Test
	void shouldGetJobFolder() {
		final Job job = Instancio.create(Job.class);
		
		final String expectedFolder = job.getJobConfiguration()
				.getStorageConfiguration()
				.getFolder() +
				job.getJobConfiguration().getName();
		
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