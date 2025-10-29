package com.x.scrape.persistence.repository.job;

import com.x.scrape.model.job_definition.JobDefinition;
import com.x.scrape.persistence.config.conditionals.annotation.IsS3;
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
	
	private final Path jobPersistencePath;
	
	public JobDefinitionS3Repository(final S3PersistenceService s3PersistenceService,
									 final EntityIdSetterService entityIdSetterService,
	                                 final S3PersistenceProperties s3PersistenceProperties) {
		this.s3PersistenceService = s3PersistenceService;
		this.entityIdSetterService = entityIdSetterService;
		jobPersistencePath = Path.of(s3PersistenceProperties.getFolder() + JOB_DEFINITION_PERSISTENCE_SUB_PATH);
	}
	
	@Override
	public JobDefinition save(final JobDefinition jobDefinition) {
		entityIdSetterService.setIds(jobDefinition);
		final Path jobDefinitionPath = Path.of(jobPersistencePath.toString() + "/" + jobDefinition.getId() + "/" + JOB_DEFINITION_FILE_NAME);
		
		return s3PersistenceService.putObject(jobDefinitionPath, jobDefinition);
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
