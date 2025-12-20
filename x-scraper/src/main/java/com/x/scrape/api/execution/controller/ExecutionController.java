package com.x.scrape.api.execution.controller;

import com.x.scrape.api.execution.dto.ReadJobExecutionDto;
import com.x.scrape.api.execution.service.ExecutionApiService;
import com.x.scrape.execution.model.Job;
import com.x.scrape.model.job_definition.JobDefinition;
import com.x.scrape.service.exception.JobDefinitionNotFoundException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

// TODO - Rename job id -> jobDefinitionId as that actually represents the id this is. Do not rename to jobDefinitionId in the API specs, users should not be bothered with underlying architecture.
// Todo - this entire service should support multiple running jobs and executions should be stopped/resumed/paused based on the job execution id.
// Because there should be support for multiple running jobs per job definition this should be included in V1.0
@RestController
@RequestMapping("/jobs/{jobId}")
public class ExecutionController {
	
	private final ExecutionApiService executionApiService;
	
	public ExecutionController(final ExecutionApiService executionApiService) {
		this.executionApiService = executionApiService;
	}
	
	@PostMapping("/start")
	public ResponseEntity<Void> start(@PathVariable("jobId") final Long jobId) {
		executionApiService.start(jobId);
		
		return ResponseEntity.noContent()
				.build();
	}
	
	@ExceptionHandler(JobDefinitionNotFoundException.class)
	public ResponseEntity<Void> handle() {
		return ResponseEntity.notFound()
				.build();
	}
	
	@PostMapping("/stop")
	public ResponseEntity<Void> stop(@PathVariable("jobId") final Long jobId) {
		executionApiService.stop(jobId);
		
		return ResponseEntity.noContent()
				.build();
	}
	
	@PostMapping("/pause")
	public ResponseEntity<Void> pause(@PathVariable("jobId") final Long jobId) {
		executionApiService.pause(jobId);
		
		return ResponseEntity.noContent()
				.build();
	}
	
	@PostMapping("/resume")
	public ResponseEntity<Void> resume(@PathVariable("jobId") final Long jobId) {
		executionApiService.resume(jobId);
		
		return ResponseEntity.noContent()
				.build();
	}
	
	// TODO - consider if including all tasks is too slow/heavy
	// And if we need a separate endpoint for retrieving detailed information about an execution per id
	
	/**
	 * Retrieves the latest execution of a specific {@link JobDefinition}.
	 *
	 * @param jobId the id of the {@link JobDefinition}
	 * @return the latest execution.
	 */
	@GetMapping("/executions/latest")
	public ResponseEntity<ReadJobExecutionDto> getLatestExecution(@PathVariable("jobId") final Long jobId) {
		return ResponseEntity.of(executionApiService.getLatestJobExecution(jobId));
	}
	
	/**
	 * Retrieves all executions of a {@link Job}
	 *
	 * @param jobId the id of the {@link JobDefinition}
	 * @return a list containing all executions of the job
	 */
	@GetMapping("/executions")
	public List<ReadJobExecutionDto> getExecutions(@PathVariable("jobId") final Long jobId) {
		return executionApiService.getExecutions(jobId);
	}
}
