package com.x.scrape.persistence.store.job;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.x.scrape.model.job.JobDefinition;
import com.x.scrape.persistence.config.conditionals.IsFileSystem;
import com.x.scrape.persistence.file_system.service.FileSystemService;
import com.x.scrape.properties.persistence.PersistenceProperties;
import org.springframework.stereotype.Component;

import java.nio.file.Path;
import java.util.List;
import java.util.Optional;

@IsFileSystem
@Component
public class JobFileSystemRepository extends FileSystemService implements JobRepository {
	
	private static final String JOB_PERSISTENCE_SUB_PATH = "/jobs";
	
	private final Path jobPersistencePath;
	
	public JobFileSystemRepository(final PersistenceProperties persistenceProperties,
	                               final ObjectMapper objectMapper) {
		super(objectMapper);
		jobPersistencePath = Path.of(persistenceProperties.getFileSystem().getFolder() + JOB_PERSISTENCE_SUB_PATH);
	}
	
	@Override
	public JobDefinition save(final JobDefinition jobDefinition) {
		return null;
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
