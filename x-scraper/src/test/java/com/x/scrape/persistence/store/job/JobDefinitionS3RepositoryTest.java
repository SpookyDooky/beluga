package com.x.scrape.persistence.store.job;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.x.scrape.model.job_definition.JobDefinition;
import com.x.scrape.persistence.shared.service.EntityIdSetterService;
import com.x.scrape.properties.persistence.S3PersistenceProperties;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import software.amazon.awssdk.services.s3.S3Client;

import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class JobDefinitionS3RepositoryTest {
	
	private static final String S3_PERSISTENCE_FOLDER = "\\folder";
	
	private static final String JOB_DEFINITION_PERSISTENCE_SUB_PATH = "\\job-definitions";
	private static final String JOB_DEFINITION_FILE_NAME = "job-definition.json";
	
	@Mock
	private S3Client s3Client;
	@Mock
	private EntityIdSetterService entityIdSetterService;
	@Mock
	private ObjectMapper objectMapper;
	@Mock
	private S3PersistenceProperties s3PersistenceProperties;
	
	private JobDefinitionS3Repository jobDefinitionS3Repository;
	
	@Captor
	private ArgumentCaptor<Path> pathArgumentCaptor;
	
	@BeforeEach
	void setup() {
		when(s3PersistenceProperties.getFolder()).thenReturn(S3_PERSISTENCE_FOLDER);
		jobDefinitionS3Repository = spy(new JobDefinitionS3Repository(
				s3Client,
				entityIdSetterService,
				objectMapper,
				s3PersistenceProperties
		));
	}
	
	@Test
	void shouldSave() {
		final JobDefinition jobDefinition = mock();
		doReturn(jobDefinition).when(jobDefinitionS3Repository).putObject(pathArgumentCaptor.capture(), eq(jobDefinition));
		
		final Long jobDefinitionId = 123L;
		when(jobDefinition.getId()).thenReturn(jobDefinitionId);
		
		final JobDefinition result = jobDefinitionS3Repository.save(jobDefinition);
		
		assertSame(jobDefinition, result);
		verify(entityIdSetterService).setIds(jobDefinition);
		
		final Path path = pathArgumentCaptor.getValue();
		assertEquals(
				S3_PERSISTENCE_FOLDER + JOB_DEFINITION_PERSISTENCE_SUB_PATH + "\\" + jobDefinitionId + "\\" + JOB_DEFINITION_FILE_NAME,
				path.toString()
		);
	}
}