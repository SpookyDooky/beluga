package com.x.scrape.persistence.repository.job;

import com.x.scrape.model.job_definition.JobDefinition;
import com.x.scrape.model.task.TaskExecution;
import com.x.scrape.persistence.config.conditionals.annotation.IsFileSystem;
import com.x.scrape.persistence.file_system.service.FileSystemService;
import com.x.scrape.persistence.repository.task_execution.TaskExecutionFileSystemRepository;
import com.x.scrape.persistence.shared.service.EntityIdSetterService;
import com.x.scrape.properties.persistence.PersistenceProperties;
import com.x.scrape.util.TimingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import java.nio.file.Path;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@IsFileSystem
@Component
public class JobDefinitionFileSystemRepository implements JobDefinitionRepository {
	
	private static final String JOB_DEFINITION_PERSISTENCE_SUB_PATH = "/job-definitions";
	private static final String JOB_DEFINITION_FILE_NAME = "job-definition.json";
	
	private final FileSystemService fileSystemService;
	private final EntityIdSetterService entityIdSetterService;
	private final TaskExecutionFileSystemRepository taskExecutionRepository;
	
	private final Path jobPersistencePath;
	
	@Autowired
	private TimingService timingService;
	
	private final UUID uuid = UUID.randomUUID();
	
	public JobDefinitionFileSystemRepository(final FileSystemService fileSystemService,
	                                         @Lazy final EntityIdSetterService entityIdSetterService,
											 final TaskExecutionFileSystemRepository taskExecutionRepository,
	                                         final PersistenceProperties persistenceProperties) {
		this.fileSystemService = fileSystemService;
		this.entityIdSetterService = entityIdSetterService;
		this.taskExecutionRepository = taskExecutionRepository;
		
		jobPersistencePath = Path.of(persistenceProperties.getFileSystem().getFolder() + JOB_DEFINITION_PERSISTENCE_SUB_PATH);
	}
	
	@Override
	public JobDefinition save(final JobDefinition jobDefinition) {
		jobDefinition.getMostRecentExecution().ifPresent((jobExecution) -> saveTaskExecutions(jobExecution.getTasks()));
		
		entityIdSetterService.setIds(jobDefinition);
		final Path jobDefinitionPath = Path.of(jobPersistencePath.toString() + "/" + jobDefinition.getId() + "/" + JOB_DEFINITION_FILE_NAME);
		
		taskExecutionRepository.createIndex(jobDefinition.getId());
		return fileSystemService.save(jobDefinition, jobDefinitionPath);
	}
	
	private void saveTaskExecutions(final List<TaskExecution> taskExecutions) {
		for (final TaskExecution taskExecution : taskExecutions) {
			taskExecutionRepository.save(taskExecution);
		}
	}
	
	@Override
	public Optional<JobDefinition> findById(final Long id) {
		final Path jobDefinitionPath = Path.of(jobPersistencePath.toString() + "/" + id + "/" + JOB_DEFINITION_FILE_NAME);
		return Optional.of(fileSystemService.readFileAs(fileSystemService.get(jobDefinitionPath).get(), JobDefinition.class));
	}
	
	@Override
	public List<JobDefinition> findAll() {
		return null;
	}
}
