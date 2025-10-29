package com.x.scrape.persistence.repository.job;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.x.scrape.model.job_definition.JobDefinition;
import com.x.scrape.persistence.config.conditionals.annotation.IsFileSystem;
import com.x.scrape.persistence.file_system.service.FileSystemService;
import com.x.scrape.persistence.shared.service.EntityIdSetterService;
import com.x.scrape.properties.persistence.PersistenceProperties;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import java.nio.file.Path;
import java.util.List;
import java.util.Optional;

@IsFileSystem
@Component
public class JobDefinitionFileSystemRepository extends FileSystemService implements JobDefinitionRepository {
	
	private static final String JOB_DEFINITION_PERSISTENCE_SUB_PATH = "/job-definitions";
	private static final String JOB_DEFINITION_FILE_NAME = "job-definition.json";
	
	private final Path jobPersistencePath;
	
	public JobDefinitionFileSystemRepository(final PersistenceProperties persistenceProperties,
	                                         final ObjectMapper objectMapper,
	                                         @Lazy final EntityIdSetterService entityIdSetterService) {
		super(objectMapper, entityIdSetterService);
		jobPersistencePath = Path.of(persistenceProperties.getFileSystem().getFolder() + JOB_DEFINITION_PERSISTENCE_SUB_PATH);
	}
	
	@Override
	public JobDefinition save(final JobDefinition jobDefinition) {
		entityIdSetterService.setIds(jobDefinition);
		final Path jobDefinitionPath = Path.of(jobPersistencePath.toString() + "/" + jobDefinition.getId() + "/" + JOB_DEFINITION_FILE_NAME);
		
		return save(jobDefinition, jobDefinitionPath);
	}
	
	@Override
	public Optional<JobDefinition> findById(final Long id) {
		return Optional.empty();
	}
	
	@Override
	public List<JobDefinition> findAll() {
		return null;
	}
}
