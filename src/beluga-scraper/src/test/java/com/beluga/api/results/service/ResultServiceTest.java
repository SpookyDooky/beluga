package com.beluga.api.results.service;

import com.beluga.api.execution.exception.TaskResultNotFoundException;
import com.beluga.api.results.dto.ResultFileInfoDto;
import com.beluga.api.results.dto.TaskResultDto;
import com.beluga.api.results.mapper.ResultFileInfoMapper;
import com.beluga.api.results.mapper.TaskResultDtoMapper;
import com.beluga.model.job_definition.JobDefinition;
import com.beluga.model.job_definition.JobExecution;
import com.beluga.model.result.ResultFile;
import com.beluga.execution.model.task.TaskExecution;
import com.beluga.result_storage.ResultStorageService;
import com.beluga.service.job.JobDefinitionService;
import com.beluga.service.task.TaskExecutionService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

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
	private TaskResultDtoMapper taskResultDtoMapper;
	@Mock
	private ResultFileInfoMapper resultFileInfoMapper;
	@Mock
	private ResultStorageService resultStorageService;
	
	@InjectMocks
	private ResultService resultService;
	
	@Captor
	private ArgumentCaptor<Pageable> pageableArgumentCaptor;
	
	@Test
	void shouldGetTaskResult() {
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
		
		final TaskResultDto taskResultDto = mock();
		when(taskResultDtoMapper.map(taskExecution)).thenReturn(taskResultDto);
		
		final TaskResultDto result = resultService.getTaskResult(
				jobDefinitionId, jobExecutionId, taskExecutionId
		);

		assertSame(taskResultDto, result);
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
	
	@Test
	void shouldGetResults() {
		final Long jobDefinitionId = 1L;
		final Long jobExecutionId = 1L;
		final int page = 0;
		final int pageSize = 1;
		
		final JobDefinition jobDefinition = mock();
		when(jobDefinitionService.getById(jobDefinitionId)).thenReturn(jobDefinition);
		
		final JobExecution jobExecution = mock();
		when(jobDefinition.getExecutionById(jobExecutionId)).thenReturn(jobExecution);
		
		final TaskExecution taskExecution = mock();
		final Page<TaskExecution> taskExecutionPage = new PageImpl<>(List.of(taskExecution));
		when(taskExecutionService.findByJobExecutionPaged(eq(jobExecution), pageableArgumentCaptor.capture())).thenReturn(taskExecutionPage);
		
		final TaskResultDto taskResultDto = mock();
		when(taskResultDtoMapper.map(taskExecution)).thenReturn(taskResultDto);
		
		final Page<TaskResultDto> result = resultService.getResults(
				jobDefinitionId,
				jobExecutionId,
				page,
				pageSize
		);
		
		final List<TaskResultDto> taskResults = result.stream().toList();
		assertEquals(1, taskResults.size());
		assertTrue(taskResults.contains(taskResultDto));
	}
	
	@Test
	void shouldGetResultFileInfo() {
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
		when(taskExecution.getResultFiles()).thenReturn(List.of(resultFile));
		
		final ResultFileInfoDto resultFileInfoDto = mock();
		when(resultFileInfoMapper.map(resultFile)).thenReturn(resultFileInfoDto);
		
		final List<ResultFileInfoDto> result = resultService.getResultFileInfo(
				jobDefinitionId,
				jobExecutionId,
				taskExecutionId
		);
		
		assertEquals(1, result.size());
		assertTrue(result.contains(resultFileInfoDto));
	}
	
	@Test
	void shouldGetResultFileContent() {
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
		
		final String fileName = "fileName";
		final String filePath = "path";
		final ResultFile resultFile = mock();
		when(taskExecution.getResultFileByFileName(fileName)).thenReturn(resultFile);
		when(resultFile.getNamespace()).thenReturn(filePath);
		
		final byte[] content = new byte[1];
		when(resultStorageService.retrieve(Path.of(filePath))).thenReturn(content);
		
		final byte[] result = resultService.getResultFileContent(
				jobDefinitionId,
				jobExecutionId,
				taskExecutionId,
				fileName
		);
		
		assertSame(content, result);
	}
}