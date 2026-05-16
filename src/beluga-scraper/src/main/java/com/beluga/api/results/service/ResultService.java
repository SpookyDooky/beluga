package com.beluga.api.results.service;

import com.beluga.api.execution.exception.TaskResultNotFoundException;
import com.beluga.api.results.dto.ResultFileInfoDto;
import com.beluga.api.results.dto.TaskResultDto;
import com.beluga.api.results.mapper.ResultFileInfoMapper;
import com.beluga.api.results.mapper.TaskResultDtoMapper;
import com.beluga.execution.model.task.Task;
import com.beluga.execution.model.task.TaskExecution;
import com.beluga.model.job_definition.JobDefinition;
import com.beluga.model.job_definition.JobExecution;
import com.beluga.model.result.ResultFile;
import com.beluga.result_storage.ResultStorageService;
import com.beluga.service.job.JobDefinitionService;
import com.beluga.service.task.TaskExecutionService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class ResultService {
	
	private final JobDefinitionService jobDefinitionService;
	private final TaskExecutionService taskExecutionService;
	private final TaskResultDtoMapper taskResultDtoMapper;
	private final ResultFileInfoMapper resultFileInfoMapper;
	private final ResultStorageService resultStorageService;
	
	public ResultService(final JobDefinitionService jobDefinitionService,
	                     final TaskExecutionService taskExecutionService,
	                     final TaskResultDtoMapper taskResultDtoMapper,
	                     final ResultFileInfoMapper resultFileInfoMapper,
	                     final ResultStorageService resultStorageService) {
		this.jobDefinitionService = jobDefinitionService;
		this.taskExecutionService = taskExecutionService;
		this.taskResultDtoMapper = taskResultDtoMapper;
		this.resultFileInfoMapper = resultFileInfoMapper;
		this.resultStorageService = resultStorageService;
	}
	
	/**
	 * Retrieves the {@link Task} result for a single task.
	 *
	 * @param jobDefinitionId id of the {@link JobDefinition} a {@link Task} belongs to.
	 * @param executionId     id of the {@link JobExecution} a {@link Task} belongs to.
	 * @param taskExecutionId id of the {@link Task}.
	 * @return the task result.
	 */
	@Transactional
	public TaskResultDto getTaskResult(final Long jobDefinitionId,
	                                   final Long executionId,
	                                   final Long taskExecutionId) {
		validateTaskResultExists(jobDefinitionId, executionId, taskExecutionId);
		
		final TaskExecution taskExecution = taskExecutionService.getById(taskExecutionId);
		return taskResultDtoMapper.map(taskExecution);
	}
	
	// TODO - Is there a better way for doing this?
	// Move to job definition service to improve testability
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
	
	@Transactional
	public Page<TaskResultDto> getResults(final Long jobDefinitionId,
	                                      final Long jobExecutionId,
	                                      final int page,
	                                      final int pageSize) {
		final JobExecution jobExecution = jobDefinitionService.getById(jobDefinitionId)
				.getExecutionById(jobExecutionId);
		
		return taskExecutionService.findByJobExecutionPaged(
				jobExecution, createPageRequest(page, pageSize)
		).map(taskResultDtoMapper::map);
	}
	
	private PageRequest createPageRequest(final int page,
	                                      final int pageSize) {
		return PageRequest.of(page, pageSize);
	}
	
	@Transactional
	public List<ResultFileInfoDto> getResultFileInfo(final Long jobDefinitionId,
	                                                 final Long executionId,
	                                                 final Long taskExecutionId) {
		validateTaskResultExists(jobDefinitionId, executionId, taskExecutionId);
		final TaskExecution taskExecution = taskExecutionService.getById(taskExecutionId);
		
		return taskExecution.getResultFiles().stream()
				.map(resultFileInfoMapper::map)
				.toList();
	}
	
	@Transactional
	public byte[] getResultFileContent(final Long jobDefinitionId,
	                                   final Long executionId,
	                                   final Long taskExecutionId,
	                                   final String fileName) {
		validateTaskResultExists(jobDefinitionId, executionId, taskExecutionId);
		final ResultFile resultFile = taskExecutionService.getById(taskExecutionId)
				.getResultFileByKey(fileName);
		
		return resultStorageService.retrieve(resultFile.getResourceIdentifier());
	}
}
