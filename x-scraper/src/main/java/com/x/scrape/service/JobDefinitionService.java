package com.x.scrape.service;

import com.x.scrape.execution.model.Job;
import com.x.scrape.mapper.job.JobMapper;
import com.x.scrape.model.job_definition.JobDefinition;
import com.x.scrape.model.job_definition.JobExecution;
import com.x.scrape.persistence.store.job.JobDefinitionRepository;
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
	
	@Transactional
	public Job createJob(final JobDefinition jobDefinition) {
		final JobExecution jobExecution = new JobExecution();
		jobDefinition.addExecution(jobExecution);
		
		save(jobDefinition);
		
		final Job job = jobMapper.map(jobDefinition);
		job.setId(jobExecution.getId());
		
		return job;
	}
	
}
