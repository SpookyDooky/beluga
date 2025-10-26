package com.x.scrape.model.job_definition;

import org.instancio.Instancio;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

class JobDefinitionTest {
	
	@Test
	void shouldCreateUniqueId() {
		final UUID id1 = new JobDefinition(mock()).getUuid();
		final UUID id2 = new JobDefinition(mock()).getUuid();
		
		assertNotEquals(id1, id2);
	}
	
	@Test
	void shouldGetJobFolder() {
		final JobDefinition jobDefinition = Instancio.create(JobDefinition.class);
		
		final String expectedFolder = jobDefinition.getJobConfiguration()
				.getStorageConfiguration()
				.getFolder() +
				jobDefinition.getJobConfiguration().getName();
		
		final String actualFolder = jobDefinition.getJobFolder();
		
		assertEquals(expectedFolder, actualFolder);
	}
	
	@Test
	void shouldGetJobTaskResultFolder() {
		final JobDefinition jobDefinition = Instancio.create(JobDefinition.class);
		
		final String expectedFolder = jobDefinition.getJobFolder()
				+ "/job-executions/" + jobDefinition.getUuid() +
				"/results/tasks";
		
		final String actualFolder = jobDefinition.getJobTaskResultsFolder();
		
		assertEquals(expectedFolder, actualFolder);
	}
	
	@Test
	void shouldAddExecution() {
		final JobExecution jobExecution = mock();
		final JobDefinition jobDefinition = new JobDefinition(null);
		
		jobDefinition.addExecution(jobExecution);
		
		assertEquals(1, jobDefinition.getExecutions().size());
		assertSame(jobExecution, jobDefinition.getExecutions().getFirst());
	}
}