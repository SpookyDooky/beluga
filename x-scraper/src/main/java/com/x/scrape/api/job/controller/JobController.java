package com.x.scrape.api.job.controller;

import com.x.scrape.api.job.dto.read.ReadJobDefinitionDto;
import com.x.scrape.api.job.dto.write.WriteJobDefinitionDto;
import com.x.scrape.logging.CloseableContext;
import com.x.scrape.logging.ContextKeys;
import com.x.scrape.logging.ContextLogger;
import com.x.scrape.mapper.job.JobDefinitionMapper;
import com.x.scrape.model.job_definition.JobDefinition;
import com.x.scrape.service.JobDefinitionService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/jobs")
public class JobController {
	
	private final ContextLogger logger;
	private final JobDefinitionMapper jobDefinitionMapper;
	private final JobDefinitionService jobDefinitionService;
	
	public JobController(final ContextLogger logger,
	                     final JobDefinitionMapper jobDefinitionMapper,
	                     final JobDefinitionService jobDefinitionService) {
		this.logger = logger;
		this.jobDefinitionMapper = jobDefinitionMapper;
		this.jobDefinitionService = jobDefinitionService;
	}
	
	// Todo - improve logging, and use AOP to automatically log all endpoint access.
	@PostMapping
	@Transactional
	public ReadJobDefinitionDto createJob(@RequestBody @Valid final WriteJobDefinitionDto job) {
		logger.info("Received new job definition.");
		
		final JobDefinition jobDefinition = jobDefinitionMapper.map(job);
		jobDefinitionService.save(jobDefinition);
		
		return jobDefinitionMapper.map(jobDefinition);
	}
	
	@GetMapping("/{id}")
	@Transactional
	public ResponseEntity<ReadJobDefinitionDto> getJob(@PathVariable final Long id) {
		try (final CloseableContext ignored = logger.with(ContextKeys.JOB_ID)) {
			logger.info("Retrieving job.");
			
			return jobDefinitionService.findById(id)
					.map(jobDefinitionMapper::map)
					.map(ResponseEntity::ok)
					.orElseGet(() -> ResponseEntity.notFound().build());
			
		}
	}
}
