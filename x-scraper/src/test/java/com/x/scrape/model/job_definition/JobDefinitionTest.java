package com.x.scrape.model.job_definition;

import jakarta.persistence.EntityNotFoundException;
import org.instancio.Instancio;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.instancio.Select.field;
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
	
	@Test
	void shouldFindExecutionById() {
		final Long executionId = 123L;
		final JobExecution jobExecution = Instancio.of(JobExecution.class)
				.set(field(JobExecution::getId), executionId)
				.create();
		
		final JobDefinition jobDefinition = Instancio.of(JobDefinition.class)
				.set(field(JobDefinition::getExecutions), List.of(jobExecution))
				.create();
		
		final JobExecution result = jobDefinition.getExecutionById(executionId);
		
		assertSame(jobExecution, result);
	}
	
	@Test
	void shouldThrowEntityNotFoundExceptionGetExecutionById() {
		final JobDefinition jobDefinition = new JobDefinition();
		
		assertThrows(EntityNotFoundException.class, () -> jobDefinition.getExecutionById(123L));
	}
}