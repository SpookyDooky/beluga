package com.x.scrape.scraping.task;

import com.x.scrape.model.job.Job;
import com.x.scrape.model.task.Task;
import org.instancio.Instancio;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.net.URL;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.Mockito.mock;

class TaskFactoryTest {
	
	private final TaskFactory taskFactory = new TaskFactory();
	
	@Test
	void shouldCreate() {
		final Job job = Instancio.create(Job.class);
		final URL url = mock();
		
		final Task task = taskFactory.create(url, job);
		
		assertSame(job, task.getJob());
		assertSame(url, task.getUrl());
//		assertEquals(job.getScrapingConfiguration(), task.getScrapingConfiguration());
//		assertEquals(job.getStorageConfiguration(), task.getStorageConfiguration());
		Assertions.fail();
	}
}