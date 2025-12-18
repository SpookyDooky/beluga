package com.x.scrape.api.execution.controller;

import com.x.scrape.api.execution.service.ExecutionApiService;
import com.x.scrape.service.exception.JobDefinitionNotFoundException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

// TODO - Rename job id -> jobDefinitionId as that actually represents the id this is. Do not rename to jobDefinitionId in the API specs, users should not be bothered with underlying architecture.
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
}
