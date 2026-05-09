package com.beluga.api.results.controller;

import com.beluga.api.execution.exception.TaskResultNotFoundException;
import com.beluga.api.results.dto.ResultFileInfoDto;
import com.beluga.api.results.dto.TaskResultDto;
import com.beluga.api.results.service.ResultService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;

import java.util.List;

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

	@Test
	void shouldGetJobExecutionTaskResults() {
		final Long jobDefinitionId = 1L;
		final Long jobExecutionId = 2L;
		final int page = 0;
		final int pageSize = 1;
		
		final Page<TaskResultDto> expectedResult = mock();
		when(resultService.getResults(
				jobDefinitionId,
				jobExecutionId,
				page,
				pageSize
		)).thenReturn(expectedResult);
		
		final Page<TaskResultDto> result = resultsController.getJobExecutionTaskResults(
				jobDefinitionId,
				jobExecutionId,
				page,
				pageSize
		);
		
		assertSame(expectedResult, result);
	}
	
	@Test
	void shouldGetResultFileInfo() {
		final Long jobDefinitionId = 1L;
		final Long jobExecutionId = 2L;
		final Long taskExecutionId = 3L;
		
		final List<ResultFileInfoDto> expected = mock();
		when(resultService.getResultFileInfo(
				jobDefinitionId,
				jobExecutionId,
				taskExecutionId
		)).thenReturn(expected);
		
		final List<ResultFileInfoDto> result = resultService.getResultFileInfo(
				jobDefinitionId,
				jobExecutionId,
				taskExecutionId
		);
		
		assertSame(expected, result);
	}
	
	@Test
	void shouldDownloadFile() {
		final Long jobDefinitionId = 1L;
		final Long jobExecutionId = 2L;
		final Long taskExecutionId = 3L;
		final String fileName = "fileName";
		
		final byte[] expected = new byte[2];
		when(resultService.getResultFileContent(
				jobDefinitionId,
				jobExecutionId,
				taskExecutionId,
				fileName
		)).thenReturn(expected);
		
		final byte[] result = resultService.getResultFileContent(
				jobDefinitionId,
				jobExecutionId,
				taskExecutionId,
				fileName
		);
		
		assertSame(expected, result);
	}
}