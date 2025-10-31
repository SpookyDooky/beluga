package com.x.scrape.service;

import com.x.scrape.mapper.job.JobMapper;
import com.x.scrape.model.job_definition.JobDefinition;
import com.x.scrape.persistence.repository.job.JobDefinitionRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static com.x.scrape.test_utils.TestReflectionUtility.assertAnnotationPresentOnMethod;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

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
}