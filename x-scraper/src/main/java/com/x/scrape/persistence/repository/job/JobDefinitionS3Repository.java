package com.x.scrape.persistence.repository.job;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.x.scrape.model.job_definition.JobDefinition;
import com.x.scrape.persistence.config.conditionals.annotation.IsS3;
import com.x.scrape.persistence.s3.service.S3PersistenceService;
import com.x.scrape.persistence.shared.service.EntityIdSetterService;
import com.x.scrape.properties.persistence.S3PersistenceProperties;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import software.amazon.awssdk.services.s3.S3Client;

import java.nio.file.Path;
import java.util.List;
import java.util.Optional;

@IsS3
@Component
public class JobDefinitionS3Repository extends S3PersistenceService implements JobDefinitionRepository {
	
	private static final String JOB_DEFINITION_PERSISTENCE_SUB_PATH = "/job-definitions";
	private static final String JOB_DEFINITION_FILE_NAME = "job-definition.json";
	
	private final Path jobPersistencePath;
	
	public JobDefinitionS3Repository(@Qualifier("persistence-s3client") final S3Client s3Client,
	                                 @Lazy final EntityIdSetterService entityIdSetterService,
	                                 final ObjectMapper objectMapper,
	                                 final S3PersistenceProperties s3PersistenceProperties) {
		super(s3Client, entityIdSetterService, objectMapper, s3PersistenceProperties);
		jobPersistencePath = Path.of(s3PersistenceProperties.getFolder() + JOB_DEFINITION_PERSISTENCE_SUB_PATH);
	}
	
	@Override
	public JobDefinition save(final JobDefinition jobDefinition) {
		entityIdSetterService.setIds(jobDefinition);
		final Path jobDefinitionPath = Path.of(jobPersistencePath.toString() + "/" + jobDefinition.getId() + "/" + JOB_DEFINITION_FILE_NAME);
		
		return putObject(jobDefinitionPath, jobDefinition);
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
