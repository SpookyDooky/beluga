package com.x.scrape.api.results.controller;

import com.x.scrape.api.execution.exception.TaskResultNotFoundException;
import com.x.scrape.api.results.dto.TaskResultDto;
import com.x.scrape.api.results.service.ResultService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ResultsControllerTest {
	
	@Mock
	private ResultService resultService;
	
	@InjectMocks
	private ResultsController resultsController;
	
	@Test
	void shouldGetTaskResults() {
		final Long jobDefinitionId = 1L;
		final Long jobExecutionId = 2L;
		final Long taskExecutionId = 3L;
		
		final TaskResultDto expected = mock();
		when(resultService.getTaskResult(
				jobDefinitionId,
				jobExecutionId,
				taskExecutionId
		)).thenReturn(expected);
		
		final TaskResultDto result = resultsController.getTaskResults(
				jobDefinitionId,
				jobExecutionId,
				taskExecutionId
		);
		
		assertSame(expected, result);
	}
	
	@Test
	void shouldReturnNotFoundOnTaskResultNotFoundException() {
		final ResponseEntity<Void> responseEntity = resultsController.handleTaskNotFoundException(new TaskResultNotFoundException());
		
		assertEquals(404, responseEntity.getStatusCode().value());
	}
}