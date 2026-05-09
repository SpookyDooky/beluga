package com.beluga.api.job.controller;

import com.beluga.api.job.dto.read.ReadJobDefinitionDto;
import com.beluga.api.job.dto.write.WriteJobDefinitionDto;
import com.beluga.logging.CloseableContext;
import com.beluga.logging.ContextKeys;
import com.beluga.logging.ContextLogger;
import com.beluga.mapper.job.JobDefinitionMapper;
import com.beluga.model.job_definition.JobDefinition;
import com.beluga.service.job.JobDefinitionService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

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
	public ReadJobDefinitionDto create(@RequestBody @Valid final WriteJobDefinitionDto job) {
		logger.info("Received new job definition.");
		
		final JobDefinition jobDefinition = jobDefinitionMapper.map(job);
		jobDefinitionService.save(jobDefinition);
		
		return jobDefinitionMapper.map(jobDefinition);
	}
	
	@GetMapping("/{id}")
	@Transactional
	public ResponseEntity<ReadJobDefinitionDto> get(@PathVariable("id") final Long id) {
		try (final CloseableContext ignored = logger.with(ContextKeys.JOB_ID)) {
			logger.info("Retrieving job.");
			
			return jobDefinitionService.findById(id)
					.map(jobDefinitionMapper::map)
					.map(ResponseEntity::ok)
					.orElseGet(() -> ResponseEntity.notFound().build());
			
		}
	}
	
	@PutMapping("/{id}")
	@Transactional
	public ResponseEntity<ReadJobDefinitionDto> update(@PathVariable("id") final Long id,
	                                                   @RequestBody @Valid final WriteJobDefinitionDto writeJobDefinitionDto) {
		try (final CloseableContext ignored = logger.with(ContextKeys.JOB_ID)) {
			logger.info("Updating job.");
			
			final Optional<JobDefinition> jobDefinitionOptional = jobDefinitionService.findById(id);
			if (jobDefinitionOptional.isEmpty()) {
				return ResponseEntity.notFound()
						.build();
			}
			
			final JobDefinition jobDefinition = jobDefinitionOptional.get();
			jobDefinitionMapper.update(writeJobDefinitionDto, jobDefinition);
			jobDefinitionService.save(jobDefinition);
			
			return ResponseEntity.ok(jobDefinitionMapper.map(jobDefinition));
		}
	}
}
