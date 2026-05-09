package com.x.scrape.model.job_definition;

import com.x.scrape.model.job_definition.exception.TaskDefinitionNotFoundException;
import com.x.scrape.execution.model.task.TaskDefinition;
import jakarta.persistence.EntityNotFoundException;
import org.instancio.Instancio;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.instancio.Select.field;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

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
	void shouldGetExecutionById() {
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
	
	@Test
	void shouldGetTaskDefinitionById() {
		final JobDefinition jobDefinition = Instancio.create(JobDefinition.class);
		final TaskDefinition taskDefinition = jobDefinition.getTaskDefinitions().getFirst();
		
		final TaskDefinition result = jobDefinition.getTaskDefinitionById(taskDefinition.getId());
		
		assertSame(taskDefinition, result);
	}
	
	@Test
	void shouldThrowTaskDefinitionNotFoundWhenGetTaskDefinitionById() {
		final JobDefinition jobDefinition = new JobDefinition();
		
		assertThrows(TaskDefinitionNotFoundException.class, () -> jobDefinition.getTaskDefinitionById(1L));
	}
	
	@Test
	void shouldSetTaskDefinitionInactiveByUrl() {
		final JobDefinition jobDefinition = Instancio.create(JobDefinition.class);
		final TaskDefinition taskDefinition = jobDefinition.getTaskDefinitions().getFirst();
		
		jobDefinition.setTaskDefinitionsInactiveByUrl(Set.of(taskDefinition.getUrl()));
		
		assertFalse(taskDefinition.isActive());
	}
	
	@Test
	void shouldAddTaskDefinitions() {
		final JobDefinition jobDefinition = new JobDefinition();
		final TaskDefinition taskDefinition = mock();
		
		jobDefinition.addTaskDefinitions(List.of(taskDefinition));
		
		assertEquals(1, jobDefinition.getTaskDefinitions().size());
		assertTrue(jobDefinition.getTaskDefinitions().contains(taskDefinition));
		
		verify(taskDefinition).setJobDefinition(jobDefinition);
	}
	
	@Test
	void shouldGetActiveTaskDefinitions() {
		final TaskDefinition activeTaskDefinition = mock();
		when(activeTaskDefinition.isActive()).thenReturn(true);
		
		final TaskDefinition inactiveTaskDefinition = mock();
		when(inactiveTaskDefinition.isActive()).thenReturn(false);
		
		final JobDefinition jobDefinition = new JobDefinition();
		jobDefinition.addTaskDefinitions(List.of(activeTaskDefinition, inactiveTaskDefinition));
		
		final List<TaskDefinition> activeTaskDefinitions = jobDefinition.getActiveTaskDefinitions();
		
		assertEquals(1, activeTaskDefinitions.size());
		assertTrue(activeTaskDefinitions.contains(activeTaskDefinition));
	}
	
	@Test
	void shouldFindExecutionById() {
		final Long jobExecutionId = 123L;
		final JobExecution jobExecution = mock();
		when(jobExecution.getId()).thenReturn(jobExecutionId);
		
		final JobDefinition jobDefinition = Instancio.of(JobDefinition.class)
				.set(field(JobDefinition::getExecutions), List.of(jobExecution))
				.create();
		
		final JobExecution result = jobDefinition.findExecutionById(jobExecutionId)
				.get();
		
		assertSame(jobExecution, result);
	}
	
	@Test
	void shouldNotFindExecutionById() {
		final JobDefinition jobDefinition = Instancio.of(JobDefinition.class)
				.ignore(field(JobDefinition::getExecutions))
				.create();
		
		final Optional<JobExecution> result = jobDefinition.findExecutionById(123L);
		
		assertTrue(result.isEmpty());
	}
	
	@Test
	void shouldHasExecutionId() {
		final JobDefinition jobDefinition = Instancio.create(JobDefinition.class);
		final Long executionId = jobDefinition.getExecutions().getFirst().getId();
		
		assertTrue(jobDefinition.hasExecutionById(executionId));
	}
}