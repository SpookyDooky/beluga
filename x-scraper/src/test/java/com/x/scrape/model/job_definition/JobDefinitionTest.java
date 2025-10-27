package com.x.scrape.model.job_definition;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
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
}