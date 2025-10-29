package com.x.scrape.service;

import com.x.scrape.execution.model.Job;
import com.x.scrape.mapper.job.JobMapper;
import com.x.scrape.model.job_definition.JobDefinition;
import com.x.scrape.model.job_definition.JobExecution;
import com.x.scrape.persistence.repository.job.JobDefinitionRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class JobDefinitionService {
	
	private final JobDefinitionRepository repository;
	private final JobMapper jobMapper;
	
	public JobDefinitionService(final JobDefinitionRepository repository,
	                            final JobMapper jobMapper) {
		this.repository = repository;
		this.jobMapper = jobMapper;
	}
	
	@Transactional
	public JobDefinition save(final JobDefinition jobDefinition) {
		return repository.save(jobDefinition);
	}
	
	// Should be done by id or in the same transaction as the JobRegistry so there should probably be another service before the
	// Job registry that saves and then creates a job based on the definition
	// If it is done by id it means we would have to retrieve the job definition first.
	@Transactional
	public Job createJobById(final Long id) {
		final JobDefinition jobDefinition = repository.findById(id)
				.orElseThrow(EntityNotFoundException::new);
		
		jobDefinition.addExecution(new JobExecution());
		save(jobDefinition);
		
		final Job job = jobMapper.map(jobDefinition);
		job.setId(jobDefinition.getMostRecentExecution().get().getId());
		
		return job;
	}
	
}
