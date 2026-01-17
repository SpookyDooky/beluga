package com.x.scrape.service.job;

import com.x.scrape.execution.model.Job;
import com.x.scrape.mapper.job.JobMapper;
import com.x.scrape.mapper.task.TaskMapper;
import com.x.scrape.model.job_definition.JobDefinition;
import com.x.scrape.model.job_definition.JobExecution;
import com.x.scrape.model.task.Task;
import com.x.scrape.model.task.TaskDefinition;
import com.x.scrape.model.task.TaskExecution;
import com.x.scrape.model.task.TaskStatus;
import com.x.scrape.service.task.TaskExecutionService;
import org.instancio.Instancio;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static com.x.scrape.model.job_definition.JobStatus.COMPLETED;
import static com.x.scrape.model.job_definition.JobStatus.PAUSED;
import static org.instancio.Select.field;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class JobServiceTest {

	@Mock
	private JobDefinitionService jobDefinitionService;
	@Mock
	private JobMapper jobMapper;
	@Mock
	private TaskMapper taskMapper;
	@Mock
	private TaskExecutionService taskExecutionService;
	
	@InjectMocks
	private JobService jobService;
	
	@Test
	void shouldCreateJobByDefinitionId() {
		final TaskDefinition taskDefinition = Instancio.of(TaskDefinition.class)
				.set(field(TaskDefinition::isActive), true)
				.create();
		final JobDefinition jobDefinition = spy(
				Instancio.of(JobDefinition.class)
						.ignore(field(JobDefinition::getExecutions))
						.set(field(JobDefinition::getTaskDefinitions), List.of(taskDefinition))
						.create()
		);
		when(jobDefinitionService.getById(jobDefinition.getId())).thenReturn(jobDefinition);
		
		final Job job = Instancio.create(Job.class);
		when(jobMapper.map(jobDefinition)).thenReturn(job);
		
		final Task task = Instancio.create(Task.class);
		when(taskMapper.map(jobDefinition, taskDefinition)).thenReturn(task);
		
		final Job result = jobService.createJobByJobDefinitionId(jobDefinition.getId());
		
		assertEquals(jobDefinition.getMostRecentExecution().get().getId(), result.getId());
		assertEquals(1, job.getTasks().size());
		assertTrue(job.getTasks().contains(task));
		
		verify(jobDefinitionService, times(2)).save(jobDefinition);
		verify(jobDefinition).addExecution(any());
	}
	
	@Test
	void shouldCreateJobByDefinitionIdWithoutEntityManager() {
		final TaskDefinition taskDefinition = Instancio.create(TaskDefinition.class);
		final JobDefinition jobDefinition = spy(
				Instancio.of(JobDefinition.class)
						.ignore(field(JobDefinition::getExecutions))
						.set(field(JobDefinition::getTaskDefinitions), List.of(taskDefinition))
						.create()
		);
		when(jobDefinitionService.getById(jobDefinition.getId())).thenReturn(jobDefinition);
		
		final Job job = Instancio.create(Job.class);
		when(jobMapper.map(jobDefinition)).thenReturn(job);
		
		final Task task = Instancio.create(Task.class);
		when(taskMapper.map(jobDefinition, taskDefinition)).thenReturn(task);
		
		final Job result = jobService.createJobByJobDefinitionId(jobDefinition.getId());
		
		assertEquals(jobDefinition.getMostRecentExecution().get().getId(), result.getId());
		assertEquals(1, job.getTasks().size());
		assertTrue(job.getTasks().contains(task));
		
		verify(jobDefinitionService, times(2)).save(jobDefinition);
		verify(jobDefinition).addExecution(any());
	}
	
	@Test
	void shouldCreateResumedJob() {
		final Long jobDefinitionId = 123L;
		final JobDefinition jobDefinition = mock();
		when(jobDefinitionService.getById(jobDefinitionId)).thenReturn(jobDefinition);
		
		final Long jobExecutionId = 123L;
		final JobExecution jobExecution = mock();
		when(jobExecution.getId()).thenReturn(jobExecutionId);
		when(jobExecution.getStatus()).thenReturn(PAUSED);
		when(jobExecution.getJobDefinition()).thenReturn(jobDefinition);
		when(jobDefinition.getMostRecentExecution()).thenReturn(Optional.of(jobExecution));
		
		final TaskExecution taskExecution = Instancio.create(TaskExecution.class);
		when(jobExecution.getTasksByStatus(TaskStatus.PAUSED)).thenReturn(List.of(taskExecution));
		
		final Job expected = new Job();
		when(jobMapper.map(jobDefinition)).thenReturn(expected);
		
		final Task task = new Task();
		when(taskMapper.map(jobDefinition, taskExecution.getTaskDefinition())).thenReturn(task);
		
		final Job result = jobService.createResumedJob(jobDefinitionId)
						.get();
		
		assertSame(expected, result);
		assertEquals(1, result.getTasks().size());
		assertTrue(result.getTasks().contains(task));
		
		assertEquals(jobExecutionId, result.getId());
		
		final Task createdTask = result.getTasks().getFirst();
		assertEquals(taskExecution.getId(), createdTask.getId());
		assertSame(result, createdTask.getJob());
	}
	
	@Test
	void shouldNotCreateResumedJobForJobWithNoExecutions() {
		final Long jobDefinitionId = 123L;
		final JobDefinition jobDefinition = mock();
		when(jobDefinitionService.getById(jobDefinitionId)).thenReturn(jobDefinition);
		when(jobDefinition.getMostRecentExecution()).thenReturn(Optional.empty());
		
		final Optional<Job> result = jobService.createResumedJob(jobDefinitionId);
		
		assertTrue(result.isEmpty());
	}
	
	@Test
	void shouldNotCreateResumedJobForCompletedJob() {
		final Long jobDefinitionId = 123L;
		final JobDefinition jobDefinition = mock();
		when(jobDefinitionService.getById(jobDefinitionId)).thenReturn(jobDefinition);
		
		final JobExecution jobExecution = mock();
		when(jobExecution.getStatus()).thenReturn(COMPLETED);
		when(jobDefinition.getMostRecentExecution()).thenReturn(Optional.of(jobExecution));
		
		final Optional<Job> result = jobService.createResumedJob(jobDefinitionId);
		
		assertTrue(result.isEmpty());
	}
	
	@Test
	void shouldNotCreatedResumedJobForPausedJobWithNoTasksLeft() {
		final Long jobDefinitionId = 123L;
		final JobDefinition jobDefinition = mock();
		when(jobDefinitionService.getById(jobDefinitionId)).thenReturn(jobDefinition);
		
		final JobExecution jobExecution = mock();
		when(jobExecution.getStatus()).thenReturn(PAUSED);
		when(jobDefinition.getMostRecentExecution()).thenReturn(Optional.of(jobExecution));
		when(jobExecution.getTasksByStatus(TaskStatus.PAUSED)).thenReturn(List.of());
		
		final Optional<Job> result = jobService.createResumedJob(jobDefinitionId);
		
		assertTrue(result.isEmpty());
	}
}