package com.x.scrape.service.job;

import com.x.scrape.mapper.job.JobMapper;
import com.x.scrape.model.job_definition.JobDefinition;
import com.x.scrape.model.job_definition.JobExecution;
import com.x.scrape.persistence.repository.job.JobDefinitionRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.transaction.annotation.Transactional;

import java.net.URL;
import java.util.Optional;
import java.util.Set;

import static com.x.scrape.model.job_definition.JobStatus.COMPLETED;
import static com.x.scrape.test_utils.TestReflectionUtility.assertAnnotationPresentOnMethod;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
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
	void shouldGetById() {
		final Long id = 123L;
		final JobDefinition jobDefinition = mock();
		when(repository.findById(id)).thenReturn(Optional.of(jobDefinition));
		
		final JobDefinition result = jobDefinitionService.getById(id);
		
		assertSame(jobDefinition, result);
	}
	
	@Test
	void shouldThrowEntityNotFoundExceptionForGetById() {
		final Long id = 123L;
		when(repository.findById(id)).thenReturn(Optional.empty());
		
		assertThrows(EntityNotFoundException.class, () -> jobDefinitionService.getById(id));
	}
	
	@Test
	void shouldHaveTransactionalAnnotationOnGetById() {
		assertAnnotationPresentOnMethod(
				JobDefinitionService.class,
				Transactional.class,
				"getById",
				Long.class
		);
	}
	
	@Test
	void shouldFindById() {
		final Long id = 123L;
		final Optional<JobDefinition> optional = Optional.empty();
		when(repository.findById(id)).thenReturn(optional);
		
		final Optional<JobDefinition> result = jobDefinitionService.findById(id);
		
		assertSame(optional, result);
	}
	
	@Test
	void shouldHaveTransactionalAnnotationOnFindById() {
		assertAnnotationPresentOnMethod(
				JobDefinitionService.class,
				Transactional.class,
				"findById",
				Long.class
		);
	}
	
	@Test
	void shouldSetTaskDefinitionsInactiveByUrl() {
		final Long jobId = 123L;
		final Set<URL> urls = Set.of(mock(URL.class));
		
		final JobDefinition jobDefinition = mock();
		when(repository.findById(jobId)).thenReturn(Optional.of(jobDefinition));
		
		jobDefinitionService.setTaskDefinitionsInactiveByUrl(jobId, urls);
		
		verify(jobDefinition).setTaskDefinitionsInactiveByUrl(urls);
		verify(repository).save(jobDefinition);
	}
	
	@Test
	void shouldSetJobExecutionStatusById() {
		final Long jobDefinitionId = 123L;
		final JobDefinition jobDefinition = mock();
		when(repository.findById(jobDefinitionId)).thenReturn(Optional.of(jobDefinition));
		
		final Long jobExecutionId = 321L;
		final JobExecution jobExecution = mock();
		when(jobDefinition.getExecutionById(jobDefinitionId)).thenReturn(jobExecution);
		
		jobDefinitionService.setJobExecutionStatusById(COMPLETED, jobDefinitionId, jobExecutionId);
		
		verify(jobExecution).setStatus(COMPLETED);
		verify(repository).save(jobDefinition);
	}
}