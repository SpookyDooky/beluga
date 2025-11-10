package com.x.scrape.persistence.repository.task_execution;

import com.x.scrape.model.job_definition.JobDefinition;
import com.x.scrape.model.job_definition.JobExecution;
import com.x.scrape.model.task.TaskExecution;
import com.x.scrape.persistence.s3.model.JobDefinitionIndex;
import com.x.scrape.persistence.s3.service.S3PersistenceService;
import com.x.scrape.persistence.shared.model.TaskExecutionIndex;
import com.x.scrape.persistence.shared.service.EntityIdSetterService;
import com.x.scrape.properties.persistence.PersistenceProperties;
import org.instancio.Instancio;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.nio.file.Path;
import java.util.Optional;
import java.util.Set;

import static org.instancio.Select.field;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Answers.RETURNS_DEEP_STUBS;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TaskExecutionS3RepositoryTest {
	
	private static final String S3_PERSISTENCE_FOLDER = "/folder";
	private static final String JOB_DEFINITION_PERSISTENCE_SUB_PATH = "/job-definitions";
	private static final String TASK_EXECUTIONS_SUB_PATH = "/task-executions";
	
	@Mock
	private S3PersistenceService s3PersistenceService;
	@Mock
	private EntityIdSetterService entityIdSetterService;
	@Mock(answer = RETURNS_DEEP_STUBS)
	private PersistenceProperties persistenceProperties;
	
	@InjectMocks
	private TaskExecutionS3Repository taskExecutionS3Repository;
	
	@Captor
	private ArgumentCaptor<TaskExecutionIndex> taskExecutionIndexArgumentCaptor;
	
	@BeforeEach
	void setup() {
		when(persistenceProperties.getS3().getFolder()).thenReturn(S3_PERSISTENCE_FOLDER);
	}
	
	@Test
	void shouldSaveAndCreateIndex() {
		final TaskExecution taskExecution = Instancio.create(TaskExecution.class);
		
		final Path taskExecutionIndexPath = Path.of(
			S3_PERSISTENCE_FOLDER + JOB_DEFINITION_PERSISTENCE_SUB_PATH + "/" + taskExecution.getJobExecution().getJobDefinition().getId() + "/task-execution-index.json"
		);
		when(s3PersistenceService.getObjectAs(taskExecutionIndexPath, TaskExecutionIndex.class)).thenReturn(Optional.empty());
		when(s3PersistenceService.putObject(eq(taskExecutionIndexPath), any(TaskExecutionIndex.class))).thenAnswer(invocation -> invocation.getArguments()[0]);
		
		final Path taskExecutionPath = Path.of(
				S3_PERSISTENCE_FOLDER + JOB_DEFINITION_PERSISTENCE_SUB_PATH + "/" +
						taskExecution.getJobExecution().getJobDefinition().getId() +
						TASK_EXECUTIONS_SUB_PATH + "/" + taskExecution.getId() + ".json"
		);
		when(s3PersistenceService.putObject(taskExecutionPath, taskExecution)).thenReturn(taskExecution);
		
		final TaskExecution result = taskExecutionS3Repository.save(taskExecution);
		
		assertSame(taskExecution, result);
		
		verify(entityIdSetterService).setIds(taskExecution);
		
		verify(s3PersistenceService).putObject(eq(taskExecutionIndexPath), taskExecutionIndexArgumentCaptor.capture());
		final TaskExecutionIndex taskExecutionIndex = taskExecutionIndexArgumentCaptor.getValue();
		assertEquals(1, taskExecutionIndex.getIds().size());
		assertTrue(taskExecutionIndex.getIds().contains(taskExecution.getId()));
	}
	
	@Test
	void shouldFindById() {
		final Long jobDefinitionId = 123L;
		final JobDefinitionIndex jobDefinitionIndex = Instancio.of(JobDefinitionIndex.class)
				.set(field(JobDefinitionIndex::getJobDefinitionIds), Set.of(jobDefinitionId))
				.create();
		
		final Path jobDefinitionIndexPath = Path.of(
				S3_PERSISTENCE_FOLDER + JOB_DEFINITION_PERSISTENCE_SUB_PATH + "/job-definition-index.json"
		);
		when(s3PersistenceService.getObjectAs(jobDefinitionIndexPath, JobDefinitionIndex.class)).thenReturn(Optional.of(jobDefinitionIndex));
		
		final TaskExecution taskExecution = Instancio.create(TaskExecution.class);
		final TaskExecutionIndex taskExecutionIndex = Instancio.of(TaskExecutionIndex.class)
				.set(field(TaskExecutionIndex::getIds), Set.of(taskExecution.getId()))
				.create();
		
		final Path taskExecutionIndexPath = Path.of(
				S3_PERSISTENCE_FOLDER + JOB_DEFINITION_PERSISTENCE_SUB_PATH + "/" + jobDefinitionId + "/task-execution-index.json"
		);
		when(s3PersistenceService.getObjectAs(taskExecutionIndexPath, TaskExecutionIndex.class)).thenReturn(Optional.of(taskExecutionIndex));
		
		final Path taskExecutionPath = Path.of(persistenceProperties.getS3().getFolder() + JOB_DEFINITION_PERSISTENCE_SUB_PATH + "/" + jobDefinitionId + TASK_EXECUTIONS_SUB_PATH + "/" + taskExecution.getId() + ".json");
		when(s3PersistenceService.getObjectAs(taskExecutionPath, TaskExecution.class)).thenReturn(Optional.of(taskExecution));
		
		final JobExecution jobExecution = mock();
		final JobDefinition jobDefinition = mock();
		when(jobDefinition.getExecutionById(taskExecution.getJobExecution().getId())).thenReturn(jobExecution);
		
		final Path jobDefinitionPath = Path.of(taskExecutionPath.getParent().getParent().toString() + "/job-definition.json");
		when(s3PersistenceService.getObjectAs(jobDefinitionPath, JobDefinition.class)).thenReturn(Optional.of(jobDefinition));
		
		final TaskExecution result = taskExecutionS3Repository.findById(taskExecution.getId())
				.get();
		
		assertSame(taskExecution, result);
		assertSame(jobExecution, result.getJobExecution());
	}
	
	@Test
	void shouldFindByIdAndReturnEmptyOptional() {
		final Long jobDefinitionId = 123L;
		final JobDefinitionIndex jobDefinitionIndex = Instancio.of(JobDefinitionIndex.class)
				.set(field(JobDefinitionIndex::getJobDefinitionIds), Set.of(jobDefinitionId))
				.create();
		
		final Path jobDefinitionIndexPath = Path.of(
				S3_PERSISTENCE_FOLDER + JOB_DEFINITION_PERSISTENCE_SUB_PATH + "/job-definition-index.json"
		);
		when(s3PersistenceService.getObjectAs(jobDefinitionIndexPath, JobDefinitionIndex.class)).thenReturn(Optional.of(jobDefinitionIndex));
		
		final TaskExecution taskExecution = Instancio.create(TaskExecution.class);
		final TaskExecutionIndex taskExecutionIndex = Instancio.of(TaskExecutionIndex.class)
				.set(field(TaskExecutionIndex::getIds), Set.of())
				.create();
		
		final Path taskExecutionIndexPath = Path.of(
				S3_PERSISTENCE_FOLDER + JOB_DEFINITION_PERSISTENCE_SUB_PATH + "/" + jobDefinitionId + "/task-execution-index.json"
		);
		when(s3PersistenceService.getObjectAs(taskExecutionIndexPath, TaskExecutionIndex.class)).thenReturn(Optional.of(taskExecutionIndex));
		
		final Optional<TaskExecution> result = taskExecutionS3Repository.findById(taskExecution.getId());
		
		assertTrue(result.isEmpty());
	}
}