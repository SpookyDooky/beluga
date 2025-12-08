package com.x.scrape.service;

import com.x.scrape.model.job_definition.JobDefinition;
import com.x.scrape.persistence.repository.job.JobDefinitionRepository;
import com.x.scrape.service.exception.JobDefinitionNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.net.URL;
import java.util.Collection;
import java.util.Optional;

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
				.orElseThrow(JobDefinitionNotFoundException::new);
	}
	
	@Transactional(propagation = MANDATORY)
	public Optional<JobDefinition> findById(final Long id) {
		return repository.findById(id);
	}
	
	@Transactional
	public void setTaskDefinitionsInactiveByUrl(final Long id,
	                                            final Collection<URL> urls) {
		final JobDefinition jobDefinition = getById(id);
		jobDefinition.setTaskDefinitionsInactiveByUrl(urls);
		
		repository.save(jobDefinition);
	}
}
