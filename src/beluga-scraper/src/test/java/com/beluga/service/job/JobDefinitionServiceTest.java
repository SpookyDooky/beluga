package com.beluga.service.job;

import com.beluga.execution.model.task.TaskDefinition;
import com.beluga.model.job_definition.JobDefinition;
import com.beluga.model.job_definition.JobExecution;
import com.beluga.model.job_definition.JobStatus;
import com.beluga.persistence.repository.JobDefinitionRepository;
import com.beluga.service.exception.JobDefinitionNotFoundException;
import com.beluga.service.task.TaskDefinitionService;
import jakarta.persistence.EntityNotFoundException;
import org.instancio.Instancio;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.transaction.annotation.Transactional;

import java.net.URI;
import java.net.URL;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static com.beluga.model.job_definition.JobStatus.COMPLETED;
import static com.beluga.test_utils.TestReflectionUtility.assertAnnotationPresentOnMethod;
import static org.instancio.Select.field;
import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.params.provider.EnumSource.Mode.EXCLUDE;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class JobDefinitionServiceTest {
	
	@Mock
	private JobDefinitionRepository repository;
	@Mock
	private TaskDefinitionService taskDefinitionService;
	
	@InjectMocks
	private JobDefinitionService jobDefinitionService;
	
	@ParameterizedTest
	@EnumSource(value = JobStatus.class, names = {"ACTIVE", "PAUSED"})
	void shouldHaveRunningJobWhenLatestExecutionIsActiveOrPaused(final JobStatus status) {
		final Long id = 123L;
		mockLatestExecutionWithStatus(id, status);

		assertTrue(jobDefinitionService.hasRunningJob(id));
	}

	@ParameterizedTest
	@EnumSource(value = JobStatus.class, names = {"ACTIVE", "PAUSED"}, mode = EXCLUDE)
	void shouldNotHaveRunningJobWhenLatestExecutionIsNotActiveOrPaused(final JobStatus status) {
		final Long id = 123L;
		mockLatestExecutionWithStatus(id, status);

		assertFalse(jobDefinitionService.hasRunningJob(id));
	}

	@Test
	void shouldNotHaveRunningJobWhenJobHasNoExecutions() {
		final Long id = 123L;
		final JobDefinition jobDefinition = mock();
		when(jobDefinition.getMostRecentExecution()).thenReturn(Optional.empty());
		when(repository.findById(id)).thenReturn(Optional.of(jobDefinition));

		assertFalse(jobDefinitionService.hasRunningJob(id));
	}

	@Test
	void shouldThrowWhenCheckingForRunningJobOfNonExistingJob() {
		final Long id = 123L;
		when(repository.findById(id)).thenReturn(Optional.empty());

		assertThrows(JobDefinitionNotFoundException.class, () -> jobDefinitionService.hasRunningJob(id));
	}

	private void mockLatestExecutionWithStatus(final Long id,
	                                           final JobStatus status) {
		final JobExecution jobExecution = new JobExecution();
		jobExecution.setStatus(status);

		final JobDefinition jobDefinition = mock();
		when(jobDefinition.getMostRecentExecution()).thenReturn(Optional.of(jobExecution));
		when(repository.findById(id)).thenReturn(Optional.of(jobDefinition));
	}

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
		when(jobDefinition.getExecutionById(jobExecutionId)).thenReturn(jobExecution);
		
		jobDefinitionService.setJobExecutionStatusById(COMPLETED, jobDefinitionId, jobExecutionId);
		
		verify(jobExecution).setStatus(COMPLETED);
		verify(repository).save(jobDefinition);
	}
	
	@Test
	void shouldAddTaskDefinitionsById() throws Exception {
		final Long jobDefinitionId = 123L;
		final JobDefinition jobDefinition = mock();
		when(repository.findById(jobDefinitionId)).thenReturn(Optional.of(jobDefinition));
		
		final URL existingUrl = URI.create("http://localhost:1234").toURL();
		final Set<URL> existingUrls = Set.of(existingUrl);
		when(taskDefinitionService.getActiveUrlsByJobDefinitionId(jobDefinitionId)).thenReturn(existingUrls);
		
		final URL newUrl = URI.create("http://localhost:12345").toURL();
		
		final TaskDefinition existingTask = Instancio.of(TaskDefinition.class)
				.set(field(TaskDefinition::getUrl), existingUrl)
				.create();
		final TaskDefinition newTask = Instancio.of(TaskDefinition.class)
				.set(field(TaskDefinition::getUrl), newUrl)
				.create();
		
		jobDefinitionService.addTaskDefinitionsById(
				List.of(existingTask, newTask),
				jobDefinitionId
		);
		
		verify(jobDefinition).addTaskDefinitions(List.of(newTask));
		verify(repository).save(jobDefinition);
	}
	
	@Test
	void shouldGetActiveTaskDefinitionsById() {
		final Long id = 123L;
		final List<TaskDefinition> expected = List.of();
		when(taskDefinitionService.getAllActiveByJobDefinitionId(id)).thenReturn(expected);
		
		final List<TaskDefinition> result = jobDefinitionService.getActiveTaskDefinitionsById(id);
		
		assertSame(expected, result);
	}

	@Test
	void shouldExistsByName() {
		final String name = "name";
		when(repository.existsByName(name)).thenReturn(true);

		assertTrue(jobDefinitionService.existsByName(name));
	}

	@Test
	void shouldDeleteByName() {
		final String name = "name";

		jobDefinitionService.deleteByName(name);

		verify(repository).deleteByName(name);
	}

	@Test
	void shouldGetNameById() {
		final JobDefinition jobDefinition = Instancio.create(JobDefinition.class);
		when(repository.findById(jobDefinition.getId())).thenReturn(Optional.of(jobDefinition));

		assertEquals(
				jobDefinition.getName(),
				jobDefinitionService.getNameById(jobDefinition.getId())
		);
	}
}