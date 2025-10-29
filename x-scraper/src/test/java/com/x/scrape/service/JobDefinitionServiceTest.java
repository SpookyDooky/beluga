package com.x.scrape.service;

import com.x.scrape.execution.model.Job;
import com.x.scrape.mapper.job.JobMapper;
import com.x.scrape.model.job_definition.JobDefinition;
import com.x.scrape.model.job_definition.JobExecution;
import com.x.scrape.persistence.repository.job.JobDefinitionRepository;
import org.instancio.Instancio;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static com.x.scrape.test_utils.TestReflectionUtility.assertAnnotationPresentOnMethod;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class JobDefinitionServiceTest {
	
	@Mock
	private JobDefinitionRepository repository;
	@Mock
	private JobMapper jobMapper;
	
	@InjectMocks
	private JobDefinitionService jobDefinitionService;
	
	@Test
	void shouldSave() {
		final JobDefinition jobDefinition = mock();
		when(repository.save(jobDefinition)).thenReturn(jobDefinition);
		
		final JobDefinition result = jobDefinitionService.save(jobDefinition);
		
		assertSame(jobDefinition, result);
	}
	
	@Test
	void shouldHaveTransactionalAnnotationOnSave() {
		assertAnnotationPresentOnMethod(
				JobDefinitionService.class,
				Transactional.class,
				"save",
				JobDefinition.class
		);
	}
	
	@Test
	void shouldCreateJob() {
		final Long jobDefinitionId = 123L;
		final JobDefinition jobDefinition = mock();
		when(repository.findById(jobDefinitionId)).thenReturn(Optional.of(jobDefinition));
		when(repository.save(jobDefinition)).thenReturn(jobDefinition);
		
		final Job job = mock();
		when(jobMapper.map(jobDefinition)).thenReturn(job);
		
		final JobExecution jobExecution = Instancio.create(JobExecution.class);
		when(jobDefinition.getMostRecentExecution()).thenReturn(Optional.of(jobExecution));
		
		final Job result = jobDefinitionService.createJobById(jobDefinitionId);
		
		verify(job).setId(jobExecution.getId());
		assertSame(job, result);
	}
}