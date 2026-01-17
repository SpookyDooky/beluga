package com.x.scrape.api.results.controller;

import com.x.scrape.api.execution.exception.TaskResultNotFoundException;
import com.x.scrape.api.results.dto.TaskResultDto;
import com.x.scrape.api.results.service.ResultService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/jobs/{jobId}/executions/{executionId}")
public class ResultsController {
	
	private final ResultService resultService;
	
	public ResultsController(final ResultService resultService) {
		this.resultService = resultService;
	}
	
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
}
