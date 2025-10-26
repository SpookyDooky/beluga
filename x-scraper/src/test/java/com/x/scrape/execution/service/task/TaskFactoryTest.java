package com.x.scrape.execution.service.task;

import com.x.scrape.execution.service.task.TaskFactory;
import com.x.scrape.model.job_definition.JobDefinition;
import com.x.scrape.model.task.Task;
import org.instancio.Instancio;
import org.junit.jupiter.api.Test;

import java.net.URL;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.Mockito.mock;

class TaskFactoryTest {
	
	private final TaskFactory taskFactory = new TaskFactory();
	
	@Test
	void shouldCreate() {
		final JobDefinition jobDefinition = Instancio.create(JobDefinition.class);
		final URL url = mock();
		
		final Task task = taskFactory.create(url, jobDefinition);
		
		assertSame(jobDefinition, task.getJob());
		assertSame(url, task.getUrl());
		assertEquals(jobDefinition.getJobConfiguration().getScrapingConfiguration(), task.getScrapingConfiguration());
		assertEquals(jobDefinition.getJobConfiguration().getStorageConfiguration(), task.getStorageConfiguration());
	}
}