package com.x.scrape.service;

import com.x.scrape.execution.model.Job;
import com.x.scrape.mapper.job.JobMapper;
import com.x.scrape.model.job_definition.JobDefinition;
import com.x.scrape.persistence.store.job.JobDefinitionRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.transaction.annotation.Transactional;

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
		final JobDefinition jobDefinition = mock();
		when(repository.save(jobDefinition)).thenReturn(jobDefinition);
		
		final Job job = mock();
		when(jobMapper.map(jobDefinition)).thenReturn(job);
		
		final Job result = jobDefinitionService.createJob(jobDefinition);
		
		verify(job).setId(null);
		assertSame(job, result);
	}
}