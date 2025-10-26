package com.x.scrape.model.job_definition;

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
	void shouldAddExecution() {
		final JobExecution jobExecution = mock();
		final JobDefinition jobDefinition = new JobDefinition(null);
		
		jobDefinition.addExecution(jobExecution);
		
		assertEquals(1, jobDefinition.getExecutions().size());
		assertSame(jobExecution, jobDefinition.getExecutions().getFirst());
	}
}