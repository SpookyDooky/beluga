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
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;
import java.util.Optional;

@Service
public class ResultService {
	
	private static final String DATA_FILE_NAME = "data.json";
	
	private final JobDefinitionService jobDefinitionService;
	private final TaskExecutionService taskExecutionService;
	private final StorageService storageService;
	private final ObjectMapper objectMapper;
	
	public ResultService(final JobDefinitionService jobDefinitionService,
	                     final TaskExecutionService taskExecutionService,
	                     final StorageService storageService,
	                     final ObjectMapper objectMapper) {
		this.jobDefinitionService = jobDefinitionService;
		this.taskExecutionService = taskExecutionService;
		this.storageService = storageService;
		this.objectMapper = objectMapper;
	}
	
	@Transactional
	public TaskResultDto getTaskResult(final Long jobDefinitionId,
	                          final Long executionId,
	                          final Long taskExecutionId) {
		validateTaskResultExists(jobDefinitionId, executionId, taskExecutionId);
		
		// TODO - Should we validate whether the task execution is completed?
		final TaskExecution taskExecution = taskExecutionService.getById(taskExecutionId);
		final ResultFile resultFile = taskExecution.getResultFiles().stream()
				.filter(taskResultFile -> taskResultFile.getFileName().equals(DATA_FILE_NAME))
				.findFirst()
				.orElseThrow(TaskResultNotFoundException::new);
		
		final TaskResultDto result = map(resultFile);
		result.setTaskId(taskExecutionId);
		
		return result;
	}
	
	// TODO - Is there a better way for doing this?
	private void validateTaskResultExists(final Long jobDefinitionId,
	                                      final Long executionId,
	                                      final Long taskExecutionId) {
		final Optional<JobDefinition> jobDefinitionOptional = jobDefinitionService.findById(jobDefinitionId);
		
		if (jobDefinitionOptional.isEmpty()) {
			throw new TaskResultNotFoundException();
		}
		
		final JobDefinition jobDefinition = jobDefinitionOptional.get();
		if (!jobDefinition.hasExecutionById(executionId)) {
			throw new TaskResultNotFoundException();
		}
		
		final Optional<TaskExecution> taskExecutionOptional = taskExecutionService.findById(taskExecutionId);
		if (taskExecutionOptional.isEmpty()) {
			throw new TaskResultNotFoundException();
		}
		
		final TaskExecution taskExecution = taskExecutionOptional.get();
		if (!taskExecution.getJobExecution().getId().equals(executionId)) {
			throw new TaskResultNotFoundException();
		}
	}
	
	private TaskResultDto map(final ResultFile resultFile) {
		final byte[] rawResultData = storageService.retrieve(Path.of(resultFile.getPath()));
		try {
			final List<Object> data = objectMapper.readValue(rawResultData, List.class);
			
			final TaskResultDto taskResultDto = new TaskResultDto();
			
			taskResultDto.setData(data);
			
			return taskResultDto;
		} catch (final IOException e) {
			throw new IllegalStateException(e);
		}
		
	}
}
