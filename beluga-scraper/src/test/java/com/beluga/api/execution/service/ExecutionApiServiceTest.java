package com.beluga.api.execution.service;

import com.beluga.api.execution.dto.ReadJobExecutionDto;
import com.beluga.api.execution.dto.ReadJobExecutionWithTasksDto;
import com.beluga.api.execution.mapper.ReadJobExecutionMapper;
import com.beluga.execution.model.job.Job;
import com.beluga.execution.service.job.JobExecutionService;
import com.beluga.model.job_definition.JobDefinition;
import com.beluga.model.job_definition.JobExecution;
import com.beluga.model.job_definition.JobStatus;
import com.beluga.service.job.JobDefinitionService;
import com.beluga.service.job.JobService;
import org.instancio.Instancio;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import java.util.List;
import java.util.Optional;

import static com.beluga.model.job_definition.JobStatus.*;
import static org.instancio.Select.field;
import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.params.provider.EnumSource.Mode.EXCLUDE;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ExecutionApiServiceTest {
	
	@Mock
	private JobDefinitionService jobDefinitionService;
	@Mock
	private JobService jobService;
	@Mock
	private JobExecutionService jobExecutionService;
	@Mock
	private ReadJobExecutionMapper readJobExecutionMapper;
	
	@InjectMocks
	private ExecutionApiService executionApiService;
	
	@Captor
	private ArgumentCaptor<TransactionSynchronization> transactionSynchronizationArgumentCaptor;
	
	@Test
	void shouldStart() {
		final Long jobDefinitionId = 123L;
		final Job job = mock();
		when(jobService.createJobByJobDefinitionId(jobDefinitionId)).thenReturn(job);
		
		try (final MockedStatic<TransactionSynchronizationManager> transactionSynchronizationManagerMockedStatic = mockStatic(TransactionSynchronizationManager.class)) {
			executionApiService.start(jobDefinitionId);
			
			transactionSynchronizationManagerMockedStatic.verify(() -> TransactionSynchronizationManager.registerSynchronization(transactionSynchronizationArgumentCaptor.capture()));
			final TransactionSynchronization transactionSynchronization = transactionSynchronizationArgumentCaptor.getValue();
			
			transactionSynchronization.afterCommit();
			verify(jobExecutionService).executeJob(job);
		}
	}
	
	@Test
	void shouldStop() {
		final Long jobDefinitionId = 123L;
		final JobDefinition jobDefinition = mock();
		when(jobDefinitionService.getById(jobDefinitionId)).thenReturn(jobDefinition);
		
		final JobExecution jobExecution = Instancio.of(JobExecution.class)
				.set(field(JobExecution::getStatus), ACTIVE)
				.create();
		when(jobDefinition.getMostRecentExecution()).thenReturn(Optional.of(jobExecution));
		
		executionApiService.stop(jobDefinitionId);
		
		verify(jobDefinitionService).setJobExecutionStatusById(STOPPED, jobDefinitionId, jobExecution.getId());
		verify(jobExecutionService).stop(jobExecution.getId());
	}
	
	@Test
	void shouldNotStopIfExecutionIsAlreadyCompleted() {
		final Long jobDefinitionId = 123L;
		final JobDefinition jobDefinition = mock();
		when(jobDefinitionService.getById(jobDefinitionId)).thenReturn(jobDefinition);
		
		final JobExecution jobExecution = Instancio.of(JobExecution.class)
				.set(field(JobExecution::getStatus), COMPLETED)
				.create();
		when(jobDefinition.getMostRecentExecution()).thenReturn(Optional.of(jobExecution));
		
		executionApiService.stop(jobDefinitionId);
		
		verify(jobDefinitionService, never()).setJobExecutionStatusById(STOPPED, jobDefinitionId, jobExecution.getId());
		verifyNoInteractions(jobExecutionService);
	}
	
	@Test
	void shouldNotStopIfNoExecutionPresent() {
		final Long jobDefinitionId = 123L;
		final JobDefinition jobDefinition = mock();
		when(jobDefinitionService.getById(jobDefinitionId)).thenReturn(jobDefinition);
		when(jobDefinition.getMostRecentExecution()).thenReturn(Optional.empty());
		
		executionApiService.stop(jobDefinitionId);
		
		verify(jobDefinitionService, never()).setJobExecutionStatusById(any(), any(), any());
		verifyNoInteractions(jobExecutionService);
	}
	
	@Test
	void shouldPauseJob() {
		final Long jobDefinitionId = 123L;
		final JobDefinition jobDefinition = mock();
		when(jobDefinitionService.getById(jobDefinitionId)).thenReturn(jobDefinition);
		
		final JobExecution jobExecution = Instancio.of(JobExecution.class)
				.set(field(JobExecution::getStatus), ACTIVE)
				.create();
		when(jobDefinition.getMostRecentExecution()).thenReturn(Optional.of(jobExecution));
		
		executionApiService.pause(jobDefinitionId);
		
		verify(jobDefinitionService).setJobExecutionStatusById(PAUSED, jobDefinitionId, jobExecution.getId());
		verify(jobExecutionService).pause(jobExecution.getId());
	}
	
	@Test
	void shouldNotPauseJobIfExecutionAlreadyCompleted() {
		final Long jobDefinitionId = 123L;
		final JobDefinition jobDefinition = mock();
		when(jobDefinitionService.getById(jobDefinitionId)).thenReturn(jobDefinition);
		
		final JobExecution jobExecution = Instancio.of(JobExecution.class)
				.set(field(JobExecution::getStatus), COMPLETED)
				.create();
		when(jobDefinition.getMostRecentExecution()).thenReturn(Optional.of(jobExecution));
		
		executionApiService.stop(jobDefinitionId);
		
		verify(jobDefinitionService, never()).setJobExecutionStatusById(PAUSED, jobDefinitionId, jobExecution.getId());
		verifyNoInteractions(jobExecutionService);
	}
	
	@Test
	void shouldNotPauseJobIfNoExecutionPresent() {
		final Long jobDefinitionId = 123L;
		final JobDefinition jobDefinition = mock();
		when(jobDefinitionService.getById(jobDefinitionId)).thenReturn(jobDefinition);
		when(jobDefinition.getMostRecentExecution()).thenReturn(Optional.empty());
		
		executionApiService.pause(jobDefinitionId);
		
		verify(jobDefinitionService, never()).setJobExecutionStatusById(any(), any(), any());
		verifyNoInteractions(jobExecutionService);
	}
	
	@Test
	void shouldResume() {
		final Long jobDefinitionId = 123L;
		final JobDefinition jobDefinition = mock();
		when(jobDefinitionService.getById(jobDefinitionId)).thenReturn(jobDefinition);
		
		final JobExecution jobExecution = mock();
		when(jobExecution.getStatus()).thenReturn(PAUSED);
		when(jobDefinition.getMostRecentExecution()).thenReturn(Optional.of(jobExecution));
		
		final Job job = mock();
		when(jobService.createResumedJob(jobDefinitionId)).thenReturn(Optional.of(job));
		
		executionApiService.resume(jobDefinitionId);
		
		verify(jobExecutionService).executeJob(job);
	}
	
	@Test
	void shouldNotResumeIfJobHasNoExecution() {
		final Long jobDefinitionId = 123L;
		final JobDefinition jobDefinition = mock();
		when(jobDefinitionService.getById(jobDefinitionId)).thenReturn(jobDefinition);
		when(jobDefinition.getMostRecentExecution()).thenReturn(Optional.empty());
		
		executionApiService.resume(jobDefinitionId);
		
		verifyNoInteractions(jobExecutionService);
	}
	
	@ParameterizedTest
	@EnumSource(
			value = JobStatus.class,
			mode = EXCLUDE,
			names = {
					"PAUSED"
			}
	)
	void shouldNotResumeIfLastExecutionIsNotPaused(final JobStatus status) {
		final Long jobDefinitionId = 123L;
		final JobDefinition jobDefinition = mock();
		when(jobDefinitionService.getById(jobDefinitionId)).thenReturn(jobDefinition);
		
		final JobExecution jobExecution = mock();
		when(jobExecution.getStatus()).thenReturn(status);
		when(jobDefinition.getMostRecentExecution()).thenReturn(Optional.of(jobExecution));
		
		executionApiService.resume(jobDefinitionId);
		
		verifyNoInteractions(jobExecutionService);
	}
	
	@Test
	void shouldNotResumeIfJobServiceReturnsEmptyOptional() {
		final Long jobDefinitionId = 123L;
		final JobDefinition jobDefinition = mock();
		when(jobDefinitionService.getById(jobDefinitionId)).thenReturn(jobDefinition);
		
		final JobExecution jobExecution = mock();
		when(jobExecution.getStatus()).thenReturn(PAUSED);
		when(jobDefinition.getMostRecentExecution()).thenReturn(Optional.of(jobExecution));
		
		when(jobService.createResumedJob(jobDefinitionId)).thenReturn(Optional.empty());
		
		executionApiService.resume(jobDefinitionId);
		
		verifyNoInteractions(jobExecutionService);
	}
	
	@Test
	void shouldGetLatestJobExecution() {
		final JobDefinition jobDefinition = Instancio.create(JobDefinition.class);
		when(jobDefinitionService.getById(jobDefinition.getId())).thenReturn(jobDefinition);
		
		final ReadJobExecutionDto expected = mock();
		when(readJobExecutionMapper.map(jobDefinition.getMostRecentExecution().get())).thenReturn(expected);
		
		final ReadJobExecutionDto result = executionApiService.getLatestJobExecution(jobDefinition.getId())
				.get();
		
		assertSame(expected, result);
	}
	
	@Test
	void shouldNotGetLatestJobExecutionIfNoExecutionPresent() {
		final Long jobDefinitionId = 123L;
		final JobDefinition jobDefinition = mock();
		when(jobDefinitionService.getById(jobDefinitionId)).thenReturn(jobDefinition);
		when(jobDefinition.getMostRecentExecution()).thenReturn(Optional.empty());
		
		final Optional<ReadJobExecutionDto> result = executionApiService.getLatestJobExecution(jobDefinitionId);
		
		assertTrue(result.isEmpty());
	}
	
	@Test
	void shouldGetExecutions() {
		final Long jobDefinitionId = 123L;
		final JobDefinition jobDefinition = mock();
		when(jobDefinitionService.getById(jobDefinitionId)).thenReturn(jobDefinition);
		
		final JobExecution jobExecution = mock();
		final ReadJobExecutionDto readJobExecutionDto = mock();
		when(readJobExecutionMapper.map(jobExecution)).thenReturn(readJobExecutionDto);
		when(jobDefinition.getExecutions()).thenReturn(List.of(jobExecution));
		
		final List<ReadJobExecutionDto> result = executionApiService.getExecutions(jobDefinitionId);
		
		assertEquals(1, result.size());
		assertTrue(result.contains(readJobExecutionDto));
	}
	
	@Test
	void shouldGetExecution() {
		final Long jobDefinitionId = 123L;
		final JobDefinition jobDefinition = mock();
		when(jobDefinitionService.getById(jobDefinitionId)).thenReturn(jobDefinition);
		
		final Long jobExecutionId = 123L;
		final JobExecution jobExecution = mock();
		when(jobDefinition.findExecutionById(jobExecutionId)).thenReturn(Optional.of(jobExecution));
		
		final ReadJobExecutionWithTasksDto expected = mock();
		when(readJobExecutionMapper.mapWithTasks(jobExecution)).thenReturn(expected);
		
		final ReadJobExecutionWithTasksDto result = executionApiService.getExecution(jobDefinitionId, jobExecutionId)
				.get();
		
		assertSame(expected, result);
	}
	
	@Test
	void shouldNotGetExecution() {
		final Long jobDefinitionId = 123L;
		final JobDefinition jobDefinition = mock();
		when(jobDefinitionService.getById(jobDefinitionId)).thenReturn(jobDefinition);
		
		final Long jobExecutionId = 123L;
		when(jobDefinition.findExecutionById(jobExecutionId)).thenReturn(Optional.empty());
		
		final Optional<ReadJobExecutionWithTasksDto> result = executionApiService.getExecution(jobDefinitionId, jobExecutionId);
		
		assertTrue(result.isEmpty());
	}
}