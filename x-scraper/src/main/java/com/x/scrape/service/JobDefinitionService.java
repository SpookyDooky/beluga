package com.x.scrape.service;

import com.x.scrape.execution.model.Job;
import com.x.scrape.mapper.job.JobMapper;
import com.x.scrape.model.job_definition.JobDefinition;
import com.x.scrape.model.job_definition.JobExecution;
import com.x.scrape.persistence.repository.job.JobDefinitionRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.transaction.annotation.Propagation.MANDATORY;

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
	
	/**
	 * Creates a job that needs to be executed.
	 * @param id job definition id.
	 * @return a new {@link Job}
	 */
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
	
	@Transactional(propagation = MANDATORY)
	public JobDefinition getById(final Long id) {
		return repository.findById(id)
				.orElseThrow(EntityNotFoundException::new);
	}
}
