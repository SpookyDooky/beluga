package com.x.scrape.persistence.repository.task_execution;

import com.x.scrape.model.job_definition.JobDefinition;
import com.x.scrape.model.job_definition.JobExecution;
import com.x.scrape.model.task.TaskExecution;
import com.x.scrape.persistence.config.conditionals.annotation.IsFileSystem;
import com.x.scrape.persistence.file_system.service.FileSystemService;
import com.x.scrape.persistence.shared.model.TaskExecutionIndex;
import com.x.scrape.persistence.shared.service.EntityIdSetterService;
import com.x.scrape.properties.persistence.PersistenceProperties;
import org.springframework.stereotype.Service;

import java.io.File;
import java.nio.file.Path;
import java.util.List;
import java.util.Optional;

@IsFileSystem
@Service
public class TaskExecutionFileSystemRepository implements TaskExecutionRepository {
	
	private static final String JOB_DEFINITION_PERSISTENCE_SUB_PATH = "/job-definitions";
	private static final String TASK_EXECUTIONS_SUB_PATH = "/task-executions";
	
	private final FileSystemService fileSystemService;
	private final PersistenceProperties persistenceProperties;
	private final EntityIdSetterService entityIdSetterService;
	
	public TaskExecutionFileSystemRepository(final FileSystemService fileSystemService,
	                                         final PersistenceProperties persistenceProperties,
	                                         final EntityIdSetterService entityIdSetterService) {
		this.fileSystemService = fileSystemService;
		this.persistenceProperties = persistenceProperties;
		this.entityIdSetterService = entityIdSetterService;
	}
	
	@Override
	public TaskExecution save(final TaskExecution taskExecution) {
		entityIdSetterService.setIds(taskExecution);
		
		final Path taskExecutionPath = createTaskExecutionPersistencePath(taskExecution);
		
		return fileSystemService.save(taskExecution, taskExecutionPath);
	}
	
	private Path createTaskExecutionPersistencePath(final TaskExecution taskExecution) {
		return Path.of(
				persistenceProperties.getFileSystem().getFolder() +
						JOB_DEFINITION_PERSISTENCE_SUB_PATH + "/" +
						taskExecution.getJobExecution().getJobDefinition().getId() +
						TASK_EXECUTIONS_SUB_PATH + "/" + taskExecution.getId() + ".json"
		);
	}
	
	@Override
	public Optional<TaskExecution> findById(final Long id) {
		final Path jobDefinitionsPath = Path.of(persistenceProperties.getFileSystem().getFolder() + JOB_DEFINITION_PERSISTENCE_SUB_PATH);
		final List<File> files = fileSystemService.listFiles(jobDefinitionsPath);
		
		// Iterate over all job definitions to scan the task execution indices
		for (final File jobDefinitionsFile : files) {
			final Path taskExecutionIndexPath = Path.of(jobDefinitionsFile.getPath() + "/" + "task-execution-index.json");
			
			final TaskExecutionIndex index = fileSystemService.readFileAs(
					taskExecutionIndexPath.toFile(),
					TaskExecutionIndex.class
			);
			
			if (index.getIds().contains(id)) {
				final Path taskExecutionPath = Path.of(jobDefinitionsFile.getAbsolutePath() + TASK_EXECUTIONS_SUB_PATH + "/" + id + ".json");
				final TaskExecution taskExecution = fileSystemService.readFileAs(taskExecutionPath.toFile(), TaskExecution.class);
				
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
		final File jobDefinitionFile = Path.of(taskExecutionPath.getParent().getParent().toString() + "/job-definition.json").toFile();
		final JobDefinition jobDefinition = fileSystemService.readFileAs(jobDefinitionFile, JobDefinition.class);
		final JobExecution jobExecution = jobDefinition.getExecutions()
				.stream().filter(execution -> execution.getId().equals(taskExecution.getJobExecution().getId()))
				.findFirst().get();
		jobExecution.setJobDefinition(jobDefinition);
		taskExecution.setJobExecution(jobExecution);
	}
	
	/**
	 * Creates a simple index file containing all the ids of all task execution to improve {@link TaskExecution} retrieval performance.
	 *
	 * @param jobDefinitionId the job definition to create the index for.
	 */
	public void createIndex(final Long jobDefinitionId) {
		final Path taskExecutionsPath = Path.of(
				persistenceProperties.getFileSystem().getFolder() + JOB_DEFINITION_PERSISTENCE_SUB_PATH
						+ "/" + jobDefinitionId + TASK_EXECUTIONS_SUB_PATH
		);
		
		final TaskExecutionIndex index = new TaskExecutionIndex();
		for (final File taskExecutionFile : fileSystemService.listFiles(taskExecutionsPath)) {
			final Long taskExecutionId = Long.valueOf(taskExecutionFile.getName().replaceAll(".json", ""));
			index.getIds().add(taskExecutionId);
		}
		
		fileSystemService.save(
				index,
				Path.of(
						persistenceProperties.getFileSystem().getFolder() + JOB_DEFINITION_PERSISTENCE_SUB_PATH
								+ "/" + jobDefinitionId + "/" + "task-execution-index.json"
				)
		);
	}
}
