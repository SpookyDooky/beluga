package com.x.scrape.persistence.repository.job;

import com.x.scrape.model.job_definition.JobDefinition;
import com.x.scrape.persistence.s3.model.JobDefinitionIndex;
import com.x.scrape.persistence.s3.service.S3PersistenceService;
import com.x.scrape.persistence.shared.service.EntityIdSetterService;
import com.x.scrape.properties.persistence.S3PersistenceProperties;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.nio.file.Path;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class JobDefinitionS3RepositoryTest {
	
	private static final String S3_PERSISTENCE_FOLDER = "\\folder";
	
	private static final String JOB_DEFINITION_PERSISTENCE_SUB_PATH = "\\job-definitions";
	private static final String JOB_DEFINITION_FILE_NAME = "job-definition.json";
	
	@Mock
	private S3PersistenceService s3PersistenceService;
	@Mock
	private EntityIdSetterService entityIdSetterService;
	@Mock
	private TaskExecutionS3Repository taskExecutionS3Repository;
	@Mock
	private S3PersistenceProperties s3PersistenceProperties;
	
	private JobDefinitionS3Repository jobDefinitionS3Repository;
	
	@Captor
	private ArgumentCaptor<Path> pathArgumentCaptor;
	
	@BeforeEach
	void setup() {
		when(s3PersistenceProperties.getFolder()).thenReturn(S3_PERSISTENCE_FOLDER);
		jobDefinitionS3Repository = spy(new JobDefinitionS3Repository(
				s3PersistenceService,
				entityIdSetterService,
				taskExecutionS3Repository,
				s3PersistenceProperties
		));
	}
	
	@Test
	void shouldSaveAndUpdateIndex() {
		final JobDefinition jobDefinition = mock();
		final Long jobDefinitionId = 123L;
		when(jobDefinition.getId()).thenReturn(jobDefinitionId);
		
		final Path jobDefinitionPath = Path.of(S3_PERSISTENCE_FOLDER + JOB_DEFINITION_PERSISTENCE_SUB_PATH + "\\" + jobDefinitionId + "\\" + JOB_DEFINITION_FILE_NAME);
		when(s3PersistenceService.putObject(jobDefinitionPath, jobDefinition)).thenReturn(jobDefinition);
		
		final JobDefinitionIndex jobDefinitionIndex = mock(RETURNS_DEEP_STUBS);
		final Path jobDefinitionIndexPath = Path.of(S3_PERSISTENCE_FOLDER + JOB_DEFINITION_PERSISTENCE_SUB_PATH + "/job-definition-index.json");
		when(s3PersistenceService.getObjectAs(jobDefinitionIndexPath, JobDefinitionIndex.class)).thenReturn(Optional.of(jobDefinitionIndex));
		when(s3PersistenceService.putObject(jobDefinitionIndexPath, jobDefinitionIndex)).thenReturn(jobDefinitionIndex);
		
		final JobDefinition result = jobDefinitionS3Repository.save(jobDefinition);
		
		assertSame(jobDefinition, result);
		
		verify(entityIdSetterService).setIds(jobDefinition);
		verify(jobDefinitionIndex.getJobDefinitionIds()).add(jobDefinitionId);
	}
	
	@Test
	void shouldSaveAndCreateIndex() {
		final JobDefinition jobDefinition = mock();
		final Long jobDefinitionId = 123L;
		when(jobDefinition.getId()).thenReturn(jobDefinitionId);
		
		final Path jobDefinitionPath = Path.of(S3_PERSISTENCE_FOLDER + JOB_DEFINITION_PERSISTENCE_SUB_PATH + "\\" + jobDefinitionId + "\\" + JOB_DEFINITION_FILE_NAME);
		when(s3PersistenceService.putObject(jobDefinitionPath, jobDefinition)).thenReturn(jobDefinition);
		
		final ArgumentCaptor<JobDefinitionIndex> jobDefinitionIndexArgumentCaptor = ArgumentCaptor.forClass(JobDefinitionIndex.class);
		final Path jobDefinitionIndexPath = Path.of(S3_PERSISTENCE_FOLDER + JOB_DEFINITION_PERSISTENCE_SUB_PATH + "/job-definition-index.json");
		when(s3PersistenceService.getObjectAs(jobDefinitionIndexPath, JobDefinitionIndex.class)).thenReturn(Optional.empty());
		when(s3PersistenceService.putObject(eq(jobDefinitionIndexPath), jobDefinitionIndexArgumentCaptor.capture())).thenAnswer(answer -> answer.getArguments()[0]);
		
		final JobDefinition result = jobDefinitionS3Repository.save(jobDefinition);
		
		assertSame(jobDefinition, result);
		
		verify(entityIdSetterService).setIds(jobDefinition);
		
		final JobDefinitionIndex jobDefinitionIndex = jobDefinitionIndexArgumentCaptor.getValue();
		assertEquals(1, jobDefinitionIndex.getJobDefinitionIds().size());
		assertTrue(jobDefinitionIndex.getJobDefinitionIds().contains(jobDefinitionId));
	}
	
	
	@Test
	void shouldFindById() {
		final Long jobDefinitionId = 123L;
		final Optional<JobDefinition> jobDefinition = mock();
		when(s3PersistenceService.getObjectAs(pathArgumentCaptor.capture(), eq(JobDefinition.class))).thenReturn(jobDefinition);
		
		final Optional<JobDefinition> result = jobDefinitionS3Repository.findById(jobDefinitionId);
		
		assertSame(jobDefinition, result);
		
		final Path path = pathArgumentCaptor.getValue();
		assertEquals(
				S3_PERSISTENCE_FOLDER + JOB_DEFINITION_PERSISTENCE_SUB_PATH + "\\" + jobDefinitionId + "\\" + JOB_DEFINITION_FILE_NAME,
				path.toString()
		);
	}
}