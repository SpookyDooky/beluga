package com.beluga.mapper.task;

import com.beluga.model.job_definition.JobDefinition;
import com.beluga.execution.model.task.Task;
import com.beluga.execution.model.task.TaskDefinition;
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
		assertEquals(jobDefinition.getStorageDefinition().getFolder(), task.getStorageConfiguration().getFolder());
		assertEquals(jobDefinition.getStorageDefinition().getFormat(), task.getStorageConfiguration().getFormat());
		assertEquals(jobDefinition.getScrapingDefinition().getItemSelector(), task.getScrapingConfiguration().getItemSelector());

		assertEquals(
				jobDefinition.getScrapingDefinition().getDataPointDefinitions().getFirst().getField(),
				task.getScrapingConfiguration().getDataPointConfigurations().getFirst().getField()
		);
	}
	
}