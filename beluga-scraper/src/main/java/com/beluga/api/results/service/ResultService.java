package com.x.scrape.api.results.service;

import com.x.scrape.api.execution.exception.TaskResultNotFoundException;
import com.x.scrape.api.results.dto.ResultFileInfoDto;
import com.x.scrape.api.results.dto.TaskResultDto;
import com.x.scrape.api.results.mapper.ResultFileInfoMapper;
import com.x.scrape.api.results.mapper.TaskResultDtoMapper;
import com.x.scrape.model.job_definition.JobDefinition;
import com.x.scrape.model.job_definition.JobExecution;
import com.x.scrape.model.result.ResultFile;
import com.x.scrape.execution.model.task.Task;
import com.x.scrape.execution.model.task.TaskExecution;
import com.x.scrape.result_storage.StorageService;
import com.x.scrape.service.job.JobDefinitionService;
import com.x.scrape.service.task.TaskExecutionService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.nio.file.Path;
import java.util.List;
import java.util.Optional;

@Service
public class ResultService {
	
	private final JobDefinitionService jobDefinitionService;
	private final TaskExecutionService taskExecutionService;
	private final TaskResultDtoMapper taskResultDtoMapper;
	private final ResultFileInfoMapper resultFileInfoMapper;
	private final StorageService storageService;
	
	public ResultService(final JobDefinitionService jobDefinitionService,
	                     final TaskExecutionService taskExecutionService,
	                     final TaskResultDtoMapper taskResultDtoMapper,
	                     final ResultFileInfoMapper resultFileInfoMapper,
	                     final StorageService storageService) {
		this.jobDefinitionService = jobDefinitionService;
		this.taskExecutionService = taskExecutionService;
		this.taskResultDtoMapper = taskResultDtoMapper;
		this.resultFileInfoMapper = resultFileInfoMapper;
		this.storageService = storageService;
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
				.getResultFileByFileName(fileName);
		
		return storageService.retrieve(Path.of(resultFile.getPath()));
	}
}
