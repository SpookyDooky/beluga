package com.x.scrape.api.results.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.x.scrape.api.execution.exception.TaskResultNotFoundException;
import com.x.scrape.api.results.dto.TaskResultDto;
import com.x.scrape.model.job_definition.JobDefinition;
import com.x.scrape.model.result.ResultFile;
import com.x.scrape.model.task.TaskExecution;
import com.x.scrape.result_storage.StorageService;
import com.x.scrape.service.job.JobDefinitionService;
import com.x.scrape.service.task.TaskExecutionService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.nio.file.Path;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ResultServiceTest {
	
	@Mock
	private JobDefinitionService jobDefinitionService;
	@Mock
	private TaskExecutionService taskExecutionService;
	@Mock
	private StorageService storageService;
	@Mock
	private ObjectMapper objectMapper;
	
	@InjectMocks
	private ResultService resultService;
	
	@Test
	void shouldGetTaskResult() throws Exception {
		final Long jobDefinitionId = 1L;
		final Long jobExecutionId = 2L;
		final Long taskExecutionId = 3L;
		
		final JobDefinition jobDefinition = mock();
		when(jobDefinitionService.findById(jobDefinitionId)).thenReturn(Optional.of(jobDefinition));
		when(jobDefinition.hasExecutionById(jobExecutionId)).thenReturn(true);
		
		final TaskExecution taskExecution = mock(RETURNS_DEEP_STUBS);
		when(taskExecutionService.findById(taskExecutionId)).thenReturn(Optional.of(taskExecution));
		when(taskExecutionService.getById(taskExecutionId)).thenReturn(taskExecution);
		when(taskExecution.getJobExecution().getId()).thenReturn(jobExecutionId);
		
		final ResultFile resultFile = mock();
		when(resultFile.getFileName()).thenReturn("data.json");
		when(resultFile.getPath()).thenReturn("path");
		when(taskExecution.getResultFiles()).thenReturn(List.of(resultFile));
		
		final byte[] rawData = new byte[1];
		when(storageService.retrieve(Path.of(resultFile.getPath()))).thenReturn(rawData);
		
		final List<Object> data = mock();
		when(objectMapper.readValue(rawData, List.class)).thenReturn(data);
		
		final TaskResultDto result = resultService.getTaskResult(
				jobDefinitionId, jobExecutionId, taskExecutionId
		);
		
		assertEquals(taskExecutionId, result.getTaskId());
		assertSame(data, result.getData());
	}
	
	/**
	 * Should throw an exception when the job definition is not found.
	 */
	@Test
	void shouldGetTaskResultNotFoundExceptionForGetTaskResult1() {
		final Long jobDefinitionId = 1L;
		when(jobDefinitionService.findById(jobDefinitionId)).thenReturn(Optional.empty());
		
		assertThrowsTaskResultNotFoundExceptionForGetTaskResult(
				jobDefinitionId,
				2L,
				3L
		);
	}
	
	void assertThrowsTaskResultNotFoundExceptionForGetTaskResult(final Long jobDefinitionId,
	                                                             final Long jobExecutionId,
	                                                             final Long taskExecutionId) {
		assertThrows(TaskResultNotFoundException.class, () -> {
			resultService.getTaskResult(
					jobDefinitionId,
					jobExecutionId,
					taskExecutionId
			);
		});
	}
	
	/**
	 * Should throw an exception when the job execution does not belong to the job definition.
	 */
	@Test
	void shouldGetTaskResultNotFoundExceptionForGetTaskResult2() {
		final Long jobDefinitionId = 1L;
		final Long jobExecutionId = 2L;
		
		final JobDefinition jobDefinition = mock();
		when(jobDefinitionService.findById(jobDefinitionId)).thenReturn(Optional.of(jobDefinition));
		when(jobDefinition.hasExecutionById(jobExecutionId)).thenReturn(false);
		
		assertThrowsTaskResultNotFoundExceptionForGetTaskResult(
				jobDefinitionId,
				jobExecutionId,
				3L
		);
	}
	
	/**
	 * Should throw an exception when the task execution does not exist.
	 */
	@Test
	void shouldGetTaskResultNotFoundExceptionForGetTaskResult3() {
		final Long jobDefinitionId = 1L;
		final Long jobExecutionId = 2L;
		final Long taskExecutionId = 3L;
		
		final JobDefinition jobDefinition = mock();
		when(jobDefinitionService.findById(jobDefinitionId)).thenReturn(Optional.of(jobDefinition));
		when(jobDefinition.hasExecutionById(jobExecutionId)).thenReturn(true);
		
		when(taskExecutionService.findById(taskExecutionId)).thenReturn(Optional.empty());
		
		assertThrowsTaskResultNotFoundExceptionForGetTaskResult(
				jobDefinitionId,
				jobExecutionId,
				taskExecutionId
		);
	}
	
	/**
	 * Should throw an exception when task execution does not belong to job execution.
	 */
	@Test
	void shouldGetTaskResultNotFoundExceptionForGetTaskResult4() {
		final Long jobDefinitionId = 1L;
		final Long jobExecutionId = 2L;
		final Long taskExecutionId = 3L;
		
		final JobDefinition jobDefinition = mock();
		when(jobDefinitionService.findById(jobDefinitionId)).thenReturn(Optional.of(jobDefinition));
		when(jobDefinition.hasExecutionById(jobExecutionId)).thenReturn(true);
		
		final TaskExecution taskExecution = mock(RETURNS_DEEP_STUBS);
		when(taskExecutionService.findById(taskExecutionId)).thenReturn(Optional.of(taskExecution));
		when(taskExecution.getJobExecution().getId()).thenReturn(4L);
		
		assertThrowsTaskResultNotFoundExceptionForGetTaskResult(
				jobDefinitionId,
				jobExecutionId,
				taskExecutionId
		);
	}
	
	/**
	 * Should throw an exception when task execution does not have a result file by the name of "data.json"
	 */
	@Test
	void shouldGetTaskResultNotFoundExceptionForGetTaskResult5() {
		final Long jobDefinitionId = 1L;
		final Long jobExecutionId = 2L;
		final Long taskExecutionId = 3L;
		
		final JobDefinition jobDefinition = mock();
		when(jobDefinitionService.findById(jobDefinitionId)).thenReturn(Optional.of(jobDefinition));
		when(jobDefinition.hasExecutionById(jobExecutionId)).thenReturn(true);
		
		final TaskExecution taskExecution = mock(RETURNS_DEEP_STUBS);
		when(taskExecutionService.findById(taskExecutionId)).thenReturn(Optional.of(taskExecution));
		when(taskExecution.getJobExecution().getId()).thenReturn(jobExecutionId);
		when(taskExecutionService.getById(taskExecutionId)).thenReturn(taskExecution);
		
		final ResultFile resultFile = mock();
		when(resultFile.getFileName()).thenReturn("data2.json");
		when(taskExecution.getResultFiles()).thenReturn(List.of(resultFile));
		
		assertThrowsTaskResultNotFoundExceptionForGetTaskResult(
				jobDefinitionId,
				jobExecutionId,
				taskExecutionId
		);
	}
}