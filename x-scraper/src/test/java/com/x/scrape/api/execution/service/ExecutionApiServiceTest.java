package com.x.scrape.api.execution.service;

import com.x.scrape.execution.model.Job;
import com.x.scrape.execution.service.job.JobExecutionService;
import com.x.scrape.model.job_definition.JobDefinition;
import com.x.scrape.model.job_definition.JobExecution;
import com.x.scrape.service.job.JobDefinitionService;
import com.x.scrape.service.job.JobService;
import org.instancio.Instancio;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import java.util.Optional;

import static com.x.scrape.model.job_definition.JobStatus.STOPPED;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ExecutionApiServiceTest {
	
	@Mock
	private JobDefinitionService jobDefinitionService;
	@Mock
	private JobService jobService;
	@Mock
	private JobExecutionService jobExecutionService;
	
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
		
		final JobExecution jobExecution = Instancio.create(JobExecution.class);
		when(jobDefinition.getMostRecentExecution()).thenReturn(Optional.of(jobExecution));
		
		executionApiService.stop(jobDefinitionId);
		
		verify(jobDefinitionService).setJobExecutionStatusById(STOPPED, jobDefinitionId, jobExecution.getId());
		verify(jobExecutionService).stop(jobExecution.getId());
	}
	
	@Test
	void shouldStopIfNoExecutionPresent() {
		final Long jobDefinitionId = 123L;
		final JobDefinition jobDefinition = mock();
		when(jobDefinitionService.getById(jobDefinitionId)).thenReturn(jobDefinition);
		when(jobDefinition.getMostRecentExecution()).thenReturn(Optional.empty());
		
		executionApiService.stop(jobDefinitionId);
		
		verify(jobDefinitionService, never()).setJobExecutionStatusById(any(), any(), any());
		verifyNoInteractions(jobExecutionService);
	}
}