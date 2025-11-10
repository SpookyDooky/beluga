package com.x.scrape.persistence.repository.job;

import com.x.scrape.model.job_definition.JobDefinition;
import com.x.scrape.model.task.TaskExecution;
import com.x.scrape.persistence.config.conditionals.annotation.IsS3;
import com.x.scrape.persistence.repository.task_execution.TaskExecutionS3Repository;
import com.x.scrape.persistence.s3.model.JobDefinitionIndex;
import com.x.scrape.persistence.s3.service.S3PersistenceService;
import com.x.scrape.persistence.shared.service.EntityIdSetterService;
import com.x.scrape.properties.persistence.S3PersistenceProperties;
import org.springframework.stereotype.Component;

import java.nio.file.Path;
import java.util.List;
import java.util.Optional;

@IsS3
@Component
public class JobDefinitionS3Repository implements JobDefinitionRepository {
	
	private static final String JOB_DEFINITION_PERSISTENCE_SUB_PATH = "/job-definitions";
	private static final String JOB_DEFINITION_FILE_NAME = "job-definition.json";
	
	private final S3PersistenceService s3PersistenceService;
	private final EntityIdSetterService entityIdSetterService;
	private final TaskExecutionS3Repository taskExecutionRepository;
	
	private final Path jobPersistencePath;
	
	public JobDefinitionS3Repository(final S3PersistenceService s3PersistenceService,
									 final EntityIdSetterService entityIdSetterService,
									 final TaskExecutionS3Repository taskExecutionRepository,
	                                 final S3PersistenceProperties s3PersistenceProperties) {
		this.s3PersistenceService = s3PersistenceService;
		this.entityIdSetterService = entityIdSetterService;
		this.taskExecutionRepository = taskExecutionRepository;
		
		jobPersistencePath = Path.of(s3PersistenceProperties.getFolder() + JOB_DEFINITION_PERSISTENCE_SUB_PATH);
	}
	
	@Override
	public JobDefinition save(final JobDefinition jobDefinition) {
		jobDefinition.getMostRecentExecution().ifPresent((jobExecution) -> saveTaskExecutions(jobExecution.getTasks()));
		
		entityIdSetterService.setIds(jobDefinition);
		final Path jobDefinitionPath = Path.of(jobPersistencePath.toString() + "/" + jobDefinition.getId() + "/" + JOB_DEFINITION_FILE_NAME);
		
		// Update job definition index to make it possible to find which job definition exists.
		updateJobDefinitionIndex(jobDefinition.getId());
		
		return s3PersistenceService.putObject(jobDefinitionPath, jobDefinition);
	}
	
	private void saveTaskExecutions(final List<TaskExecution> taskExecutions) {
		for (final TaskExecution taskExecution : taskExecutions) {
			taskExecutionRepository.save(taskExecution);
		}
	}
	
	private void updateJobDefinitionIndex(final Long jobDefinitionId) {
		final Path jobDefinitionIndexPath = Path.of(jobPersistencePath.toString() + "/job-definition-index.json");
		final JobDefinitionIndex index = s3PersistenceService.getObjectAs(jobDefinitionIndexPath, JobDefinitionIndex.class)
				.orElse(new JobDefinitionIndex());
		
		index.getJobDefinitionIds().add(jobDefinitionId);
		
		s3PersistenceService.putObject(jobDefinitionIndexPath, index);
	}
	
	@Override
	public Optional<JobDefinition> findById(final Long id) {
		final Path jobDefinitionPath = Path.of(jobPersistencePath.toString() + "/" + id + "/" + JOB_DEFINITION_FILE_NAME);
		return s3PersistenceService.getObjectAs(jobDefinitionPath, JobDefinition.class);
	}
	
	@Override
	public List<JobDefinition> findAll() {
		return null;
	}
}
