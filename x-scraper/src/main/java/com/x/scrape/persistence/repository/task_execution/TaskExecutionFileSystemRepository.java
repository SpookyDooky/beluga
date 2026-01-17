package com.x.scrape.persistence.repository.task_execution;

import com.x.scrape.model.job_definition.JobDefinition;
import com.x.scrape.model.job_definition.JobExecution;
import com.x.scrape.model.task.TaskExecution;
import com.x.scrape.persistence.config.conditionals.annotation.IsFileSystem;
import com.x.scrape.persistence.file_system.service.FileSystemService;
import com.x.scrape.persistence.shared.model.TaskExecutionIndex;
import com.x.scrape.persistence.shared.service.EntityIdSetterService;
import com.x.scrape.properties.persistence.PersistenceProperties;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.io.File;
import java.nio.file.Path;
import java.util.List;
import java.util.Optional;

@IsFileSystem
@Service
@Deprecated(forRemoval = true) // Due to SQL lite being a much better alternative
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
		
		updateJobDefinitionTaskExecutionIndex(taskExecution);
		
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
		final JobExecution jobExecution = jobDefinition.getExecutionById(taskExecution.getJobExecution().getId());
		
		jobExecution.setJobDefinition(jobDefinition);
		taskExecution.setJobExecution(jobExecution);
	}
	
	/**
	 * Update or create a new {@link TaskExecutionIndex} if it does not exist.
	 *
	 * @param taskExecution the task execution to add to the index.
	 */
	void updateJobDefinitionTaskExecutionIndex(final TaskExecution taskExecution) {
		final Path taskExecutionIndexPath = Path.of(
				persistenceProperties.getFileSystem().getFolder() + JOB_DEFINITION_PERSISTENCE_SUB_PATH
						+ "/" + taskExecution.getJobExecution().getJobDefinition().getId() + "/task-execution-index.json"
		);
		
		final Optional<File> taskExecutionIndexFileOptional = fileSystemService.get(taskExecutionIndexPath);
		final TaskExecutionIndex taskExecutionIndex = taskExecutionIndexFileOptional
				.map(taskExecutionIndexFile -> fileSystemService.readFileAs(taskExecutionIndexFile, TaskExecutionIndex.class))
				.orElse(new TaskExecutionIndex());
		
		taskExecutionIndex.getIds().add(taskExecution.getId());
		
		fileSystemService.save(taskExecutionIndex, taskExecutionIndexPath);
	}
	
	void updateJobExecutionTaskExecutionIndex(final TaskExecution taskExecution) {
		final Path jobExecutionTaskExecutionIndex = Path.of(
				persistenceProperties.getFileSystem().getFolder() + JOB_DEFINITION_PERSISTENCE_SUB_PATH
						+ "/" + taskExecution.getJobExecution().getJobDefinition().getId() + "/job-execution-task-execution-index.json"
		);
	}
	
	@Override
	public Page<TaskExecution> findByJobExecution(final JobExecution jobExecution, final Pageable pageable) {
		return null;
	}
}
