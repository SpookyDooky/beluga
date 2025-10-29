package com.x.scrape.model.job_definition;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

class JobDefinitionTest {
	
	@Test
	void shouldAddExecution() {
		final JobExecution jobExecution = mock();
		final JobDefinition jobDefinition = new JobDefinition();
		
		jobDefinition.addExecution(jobExecution);
		
		assertEquals(1, jobDefinition.getExecutions().size());
		assertSame(jobExecution, jobDefinition.getExecutions().getFirst());
	}
	
	@Test
	void shouldGetMostRecentExecution() {
		final JobExecution jobExecution = mock();
		final JobDefinition jobDefinition = new JobDefinition();
		jobDefinition.addExecution(jobExecution);
		
		final JobExecution mostRecentExecution = jobDefinition.getMostRecentExecution().get();
		
		assertSame(jobExecution, mostRecentExecution);
	}
	
	@Test
	void shouldGetMostRecentExecutionReturnsEmpty() {
		final JobDefinition jobDefinition = new JobDefinition();
		
		assertTrue(jobDefinition.getMostRecentExecution().isEmpty());
	}
}