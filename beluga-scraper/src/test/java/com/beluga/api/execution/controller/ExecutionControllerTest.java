package com.beluga.api.execution.controller;

import com.beluga.api.execution.dto.ReadJobExecutionDto;
import com.beluga.api.execution.dto.ReadJobExecutionWithTasksDto;
import com.beluga.api.execution.service.ExecutionApiService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ExecutionControllerTest {
	
	@Mock
	private ExecutionApiService executionApiService;
	
	@InjectMocks
	private ExecutionController executionController;
	
	@Test
	void shouldStart() {
		final Long jobId = 123L;
		
		final ResponseEntity<Void> response = executionController.start(jobId);
		
		assertEquals(204, response.getStatusCode().value());
		verify(executionApiService).start(jobId);
	}
	
	@Test
	void shouldStop() {
		final Long jobId = 123L;
		
		final ResponseEntity<Void> response = executionController.stop(jobId);
		
		assertEquals(204, response.getStatusCode().value());
		verify(executionApiService).stop(jobId);
	}
	
	@Test
	void shouldPause() {
		final Long jobId = 123L;
		
		final ResponseEntity<Void> response = executionController.pause(jobId);
		
		assertEquals(204, response.getStatusCode().value());
		verify(executionApiService).pause(jobId);
	}
	
	@Test
	void shouldResume() {
		final Long jobId = 123L;
		
		final ResponseEntity<Void> response = executionController.resume(jobId);
		
		assertEquals(204, response.getStatusCode().value());
		verify(executionApiService).resume(jobId);
	}
	
	@Test
	void shouldGetLatestExecution() {
		final Long jobDefinitionId = 123L;
		final ReadJobExecutionDto expected = mock();
		when(executionApiService.getLatestJobExecution(jobDefinitionId)).thenReturn(Optional.of(expected));
		
		final ResponseEntity<ReadJobExecutionDto> result = executionController.getLatestExecution(jobDefinitionId);
		
		assertSame(expected, result.getBody());
	}
	
	@Test
	void shouldGetNotFoundWhenGettingLatestExecution() {
		final Long jobDefinitionId = 321L;
		when(executionApiService.getLatestJobExecution(jobDefinitionId)).thenReturn(Optional.empty());
		
		final ResponseEntity<ReadJobExecutionDto> result = executionController.getLatestExecution(jobDefinitionId);
	
		assertEquals(404, result.getStatusCode().value());
	}
	
	@Test
	void shouldGetExecutions() {
		final Long jobDefinitionId = 543L;
		final List<ReadJobExecutionDto> expected = List.of();
		when(executionApiService.getExecutions(jobDefinitionId)).thenReturn(expected);
		
		final List<ReadJobExecutionDto> result = executionController.getExecutions(jobDefinitionId);
		
		assertSame(expected, result);
	}
	
	@Test
	void shouldGetExecution() {
		final Long jobDefinitionId = 123L;
		final Long jobExecutionId = 123L;
		
		final ReadJobExecutionWithTasksDto expected = mock();
		when(executionApiService.getExecution(jobDefinitionId, jobExecutionId)).thenReturn(Optional.of(expected));
		
		final ResponseEntity<ReadJobExecutionWithTasksDto> result = executionController.getExecution(jobDefinitionId, jobExecutionId);

		assertSame(expected, result.getBody());
	}
	
	@Test
	void shouldGetNotFoundWhenExecutionDoesNotExist() {
		final Long jobDefinitionId = 123L;
		final Long jobExecutionId = 123L;
		when(executionApiService.getExecution(jobDefinitionId, jobExecutionId)).thenReturn(Optional.empty());
		
		final ResponseEntity<ReadJobExecutionWithTasksDto> result = executionController.getExecution(jobDefinitionId, jobExecutionId);
		
		assertEquals(404, result.getStatusCode().value());
		
	}
}