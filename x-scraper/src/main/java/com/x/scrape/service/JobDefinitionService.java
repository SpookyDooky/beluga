package com.x.scrape.service;

import com.x.scrape.model.job_definition.JobDefinition;
import com.x.scrape.persistence.repository.job.JobDefinitionRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.transaction.annotation.Propagation.MANDATORY;

@Service
public class JobDefinitionService {
	
	private final JobDefinitionRepository repository;
	
	public JobDefinitionService(final JobDefinitionRepository repository) {
		this.repository = repository;
	}
	
	@Transactional
	public JobDefinition save(final JobDefinition jobDefinition) {
		return repository.save(jobDefinition);
	}
	
	@Transactional(propagation = MANDATORY)
	public JobDefinition getById(final Long id) {
		return repository.findById(id)
				.orElseThrow(EntityNotFoundException::new);
	}
}
