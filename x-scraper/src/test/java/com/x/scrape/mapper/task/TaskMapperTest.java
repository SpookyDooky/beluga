package com.x.scrape.mapper.task;

import com.x.scrape.model.job_definition.JobDefinition;
import com.x.scrape.model.task.Task;
import com.x.scrape.model.task.TaskDefinition;
import org.instancio.Instancio;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TaskMapperTest {
	
	private final TaskMapper mapper = new TaskMapperImpl();
	
	@Test
	void shouldMap() {
		final TaskDefinition taskDefinition = Instancio.create(TaskDefinition.class);
		final JobDefinition jobDefinition = Instancio.create(JobDefinition.class);
		
		final Task task = mapper.map(jobDefinition, taskDefinition);
		
		assertEquals(taskDefinition.getUrl(), task.getUrl());
		assertSame(jobDefinition.getScrapingDefinition(), task.getScrapingConfiguration());
		assertSame(jobDefinition.getStorageConfiguration(), task.getStorageConfiguration());
	}
	
}