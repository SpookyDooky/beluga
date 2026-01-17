package com.x.scrape.api.results.controller;

import com.x.scrape.api.execution.exception.TaskResultNotFoundException;
import com.x.scrape.api.results.dto.TaskResultDto;
import com.x.scrape.api.results.service.ResultService;
import com.x.scrape.model.job_definition.JobDefinition;
import com.x.scrape.model.job_definition.JobExecution;
import com.x.scrape.model.task.Task;
import com.x.scrape.model.task.TaskExecution;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/jobs/{jobId}/executions/{executionId}")
public class ResultsController {
	
	private final ResultService resultService;
	
	public ResultsController(final ResultService resultService) {
		this.resultService = resultService;
	}
	
	/**
	 * Retrieves the {@link Task}'s.
	 *
	 * @param jobDefinitionId the id of the {@link JobDefinition} this task belongs to.
	 * @param jobExecutionId  the id of the {@link JobExecution} this task belongs to.
	 * @param taskExecutionId the id of the {@link TaskExecution} to retrieve the results for.
	 * @return the task results.
	 */
	@GetMapping("/tasks/{taskId}/results")
	public TaskResultDto getTaskResults(@PathVariable("jobId") final Long jobDefinitionId,
	                                    @PathVariable("executionId") final Long jobExecutionId,
	                                    @PathVariable("taskId") final Long taskExecutionId) {
		return resultService.getTaskResult(
				jobDefinitionId,
				jobExecutionId,
				taskExecutionId
		);
	}
	
	@ExceptionHandler(TaskResultNotFoundException.class)
	public ResponseEntity<Void> handleTaskNotFoundException(final TaskResultNotFoundException exception) {
		return ResponseEntity.notFound()
				.build();
	}
	
	@GetMapping("/results")
	public Page<TaskResultDto> getJobExecutionTaskResults(@PathVariable("jobId") final Long jobDefinitionId,
	                                                      @PathVariable("executionId") final Long jobExecutionId,
	                                                      @RequestParam("page") final int page,
	                                                      @RequestParam("size") final int pageSize) {
		return null;
	}
}
