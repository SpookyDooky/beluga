package com.beluga.service.job;

import com.beluga.execution.model.job.Job;
import com.beluga.execution.model.task.Task;
import com.beluga.execution.model.task.TaskDefinition;
import com.beluga.execution.model.task.TaskExecution;
import com.beluga.execution.model.task.TaskStatus;
import com.beluga.mapper.job.JobMapper;
import com.beluga.mapper.task.TaskMapper;
import com.beluga.model.job_definition.JobDefinition;
import com.beluga.model.job_definition.JobExecution;
import com.beluga.model.job_definition.JobStatus;
import com.beluga.service.task.TaskExecutionService;
import org.instancio.Instancio;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static org.instancio.Select.field;
import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.params.provider.EnumSource.Mode.EXCLUDE;
import static org.mockito.ArgumentMatchers.any;
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
		
		when(taskExecutionService.saveAll(any())).thenAnswer(answer -> answer.getArguments()[0]);
		
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
		when(jobDefinition.getActiveTaskDefinitions()).thenReturn(List.of(taskDefinition));
		
		final Job job = Instancio.create(Job.class);
		when(jobMapper.map(jobDefinition)).thenReturn(job);
		
		final Task task = Instancio.create(Task.class);
		when(taskMapper.map(jobDefinition, taskDefinition)).thenReturn(task);
		
		when(taskExecutionService.saveAll(any())).thenAnswer(answer -> answer.getArguments()[0]);
		
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
	
	@ParameterizedTest
	@EnumSource(value = TaskStatus.class, names = {"PLANNED", "PAUSED", "ACTIVE"})
	void shouldAddUnfinishedTaskToActiveJob(final TaskStatus status) {
		final TaskExecution taskExecution = createTaskExecution(1L, status);
		final JobDefinition jobDefinition = createJobDefinitionWithLatestExecution(123L, JobStatus.ACTIVE, List.of(taskExecution));
		
		final Job expected = new Job();
		when(jobMapper.map(jobDefinition)).thenReturn(expected);
		when(taskMapper.map(jobDefinition, taskExecution.getTaskDefinition())).thenReturn(new Task());
		
		final Job result = jobService.createActiveJob(jobDefinition).get();
		
		assertSame(expected, result);
		assertEquals(123L, result.getId());
		assertEquals(1, result.getTasks().size());
		
		final Task task = result.getTasks().getFirst();
		assertEquals(taskExecution.getId(), task.getId());
		assertSame(result, task.getJob());
	}
	
	@ParameterizedTest
	@EnumSource(value = TaskStatus.class, names = {"PLANNED", "PAUSED", "ACTIVE"}, mode = EXCLUDE)
	void shouldNotAddFinishedTaskToActiveJob(final TaskStatus status) {
		final JobDefinition jobDefinition = createJobDefinitionWithLatestExecution(
				123L,
				JobStatus.ACTIVE,
				List.of(createTaskExecution(1L, status))
		);
		when(jobMapper.map(jobDefinition)).thenReturn(new Job());
		
		final Job result = jobService.createActiveJob(jobDefinition).get();
		
		assertEquals(123L, result.getId());
		assertTrue(result.getTasks().isEmpty());
	}
	
	@ParameterizedTest
	@EnumSource(value = JobStatus.class, names = "ACTIVE", mode = EXCLUDE)
	void shouldNotCreateActiveJobWhenLatestExecutionIsNotActive(final JobStatus status) {
		final JobDefinition jobDefinition = createJobDefinitionWithLatestExecution(
				123L,
				status,
				List.of(createTaskExecution(1L, TaskStatus.PLANNED))
		);

		assertTrue(jobService.createActiveJob(jobDefinition).isEmpty());
		verifyNoInteractions(jobMapper, taskMapper);
	}

	@Test
	void shouldNotCreateActiveJobForJobWithNoExecutions() {
		assertTrue(jobService.createActiveJob(new JobDefinition()).isEmpty());
		verifyNoInteractions(jobMapper, taskMapper);
	}

	@Test
	void shouldRequireTransactionForCreatingActiveJob() throws NoSuchMethodException {
		final Transactional transactional = JobService.class
				.getMethod("createActiveJob", JobDefinition.class)
				.getAnnotation(Transactional.class);

		assertEquals(Propagation.MANDATORY, transactional.propagation());
	}

	private JobDefinition createJobDefinitionWithLatestExecution(final Long jobExecutionId,
	                                                             final JobStatus status,
	                                                             final List<TaskExecution> taskExecutions) {
		final JobExecution jobExecution = new JobExecution();
		jobExecution.setId(jobExecutionId);
		jobExecution.setStatus(status);
		jobExecution.setTasks(taskExecutions);

		final JobDefinition jobDefinition = new JobDefinition();
		jobDefinition.addExecution(jobExecution);

		return jobDefinition;
	}

	private TaskExecution createTaskExecution(final Long id,
	                                          final TaskStatus status) {
		final TaskExecution taskExecution = new TaskExecution();
		taskExecution.setId(id);
		taskExecution.setStatus(status);
		taskExecution.setTaskDefinition(new TaskDefinition());

		return taskExecution;
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
}