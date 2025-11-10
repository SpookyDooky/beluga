package com.x.scrape.persistence.repository.task_execution;

import com.x.scrape.model.job_definition.JobDefinition;
import com.x.scrape.model.job_definition.JobExecution;
import com.x.scrape.model.task.TaskExecution;
import com.x.scrape.persistence.config.conditionals.annotation.IsS3;
import com.x.scrape.persistence.s3.model.JobDefinitionIndex;
import com.x.scrape.persistence.s3.service.S3PersistenceService;
import com.x.scrape.persistence.shared.model.TaskExecutionIndex;
import com.x.scrape.persistence.shared.service.EntityIdSetterService;
import com.x.scrape.properties.persistence.PersistenceProperties;
import org.springframework.stereotype.Component;

import java.nio.file.Path;
import java.util.Optional;

@IsS3
@Component
public class TaskExecutionS3Repository implements TaskExecutionRepository {
	
	private static final String JOB_DEFINITION_PERSISTENCE_SUB_PATH = "/job-definitions";
	private static final String TASK_EXECUTIONS_SUB_PATH = "/task-executions";
	
	private final S3PersistenceService s3PersistenceService;
	private final EntityIdSetterService entityIdSetterService;
	private final PersistenceProperties persistenceProperties;
	
	public TaskExecutionS3Repository(final S3PersistenceService s3PersistenceService,
	                                 final EntityIdSetterService entityIdSetterService,
	                                 final PersistenceProperties persistenceProperties) {
		this.s3PersistenceService = s3PersistenceService;
		this.entityIdSetterService = entityIdSetterService;
		this.persistenceProperties = persistenceProperties;
	}
	
	@Override
	public TaskExecution save(final TaskExecution taskExecution) {
		entityIdSetterService.setIds(taskExecution);
		updateIndex(taskExecution.getJobExecution().getJobDefinition().getId(), taskExecution.getId());
		
		final Path taskExecutionPath = createTaskExecutionPersistencePath(taskExecution);
		
		return s3PersistenceService.putObject(taskExecutionPath, taskExecution);
	}
	
	/**
	 * Creates a simple index file containing all the ids of all task execution to improve {@link TaskExecution} retrieval performance.
	 *
	 * @param jobDefinitionId the job definition to create the index for.
	 */
	private void updateIndex(final Long jobDefinitionId,
	                         final Long taskExecutionId) {
		final Path taskExecutionIndexPath = Path.of(
				persistenceProperties.getS3().getFolder() + JOB_DEFINITION_PERSISTENCE_SUB_PATH
						+ "/" + jobDefinitionId + "/" + "task-execution-index.json"
		);
		
		final TaskExecutionIndex index = s3PersistenceService.getObjectAs(taskExecutionIndexPath, TaskExecutionIndex.class)
				.orElse(new TaskExecutionIndex());
		index.getIds().add(taskExecutionId);
		
		s3PersistenceService.putObject(taskExecutionIndexPath, index);
	}
	
	private Path createTaskExecutionPersistencePath(final TaskExecution taskExecution) {
		return Path.of(
				persistenceProperties.getS3().getFolder() +
						JOB_DEFINITION_PERSISTENCE_SUB_PATH + "/" +
						taskExecution.getJobExecution().getJobDefinition().getId() +
						TASK_EXECUTIONS_SUB_PATH + "/" + taskExecution.getId() + ".json"
		);
	}
	
	@Override
	public Optional<TaskExecution> findById(final Long id) {
		final Path jobDefinitionIndexPath = Path.of(persistenceProperties.getS3().getFolder() + JOB_DEFINITION_PERSISTENCE_SUB_PATH + "/job-definition-index.json");
		final JobDefinitionIndex jobDefinitionIndex = s3PersistenceService.getObjectAs(jobDefinitionIndexPath, JobDefinitionIndex.class)
				.orElseThrow(() -> new IllegalStateException("Could not load job definition index."));
		
		for (final Long jobDefinitionId : jobDefinitionIndex.getJobDefinitionIds()) {
			final Path taskExecutionIndexPath = Path.of(persistenceProperties.getS3().getFolder() + JOB_DEFINITION_PERSISTENCE_SUB_PATH + "/" + jobDefinitionId + "/task-execution-index.json");
			final TaskExecutionIndex taskExecutionIndex = s3PersistenceService.getObjectAs(taskExecutionIndexPath, TaskExecutionIndex.class)
					.orElse(new TaskExecutionIndex());
			
			if (taskExecutionIndex.getIds().contains(id)) {
				final Path taskExecutionPath = Path.of(persistenceProperties.getS3().getFolder() + JOB_DEFINITION_PERSISTENCE_SUB_PATH + "/" + jobDefinitionId + TASK_EXECUTIONS_SUB_PATH + "/" + id + ".json");
				
				final TaskExecution taskExecution = s3PersistenceService.getObjectAs(taskExecutionPath, TaskExecution.class)
						.get();
				
				enrichTaskExecution(taskExecution, taskExecutionPath);
				
				return Optional.of(taskExecution);
			}
		}
		return Optional.empty();
	}
	
	/**
	 * Due to {@link TaskExecution}'s being stored separately from {@link JobDefinition}'s the entity needs
	 * to be enriched with the {@link JobExecution} which also needs to be linked to the {@link JobDefinition}.
	 *
	 * @param taskExecution     the execution to enrich.
	 * @param taskExecutionPath the path of the task execution.
	 */
	private void enrichTaskExecution(final TaskExecution taskExecution,
	                                 final Path taskExecutionPath) {
		final Path jobDefinitionPath = Path.of(taskExecutionPath.getParent().getParent().toString() + "/job-definition.json");
		final JobDefinition jobDefinition = s3PersistenceService.getObjectAs(jobDefinitionPath, JobDefinition.class)
				.get();
		
		final JobExecution jobExecution = jobDefinition.getExecutionById(taskExecution.getJobExecution().getId());
		
		jobExecution.setJobDefinition(jobDefinition);
		taskExecution.setJobExecution(jobExecution);
	}
}
