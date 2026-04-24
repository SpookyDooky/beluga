package com.x.scrape.service.job;

import com.x.scrape.execution.model.task.TaskDefinition;
import com.x.scrape.model.job_definition.JobDefinition;
import com.x.scrape.model.job_definition.JobExecution;
import com.x.scrape.model.job_definition.JobStatus;
import com.x.scrape.persistence.repository.JobDefinitionRepository;
import com.x.scrape.service.exception.JobDefinitionNotFoundException;
import com.x.scrape.service.task.TaskDefinitionService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.net.URL;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static java.util.function.Predicate.not;
import static org.springframework.transaction.annotation.Propagation.MANDATORY;

@Service
public class JobDefinitionService {
	
	private final JobDefinitionRepository repository;
	private final TaskDefinitionService taskDefinitionService;
	
	public JobDefinitionService(final JobDefinitionRepository repository,
	                            final TaskDefinitionService taskDefinitionService) {
		this.repository = repository;
		this.taskDefinitionService = taskDefinitionService;
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
	
	@Transactional
	public void setJobExecutionStatusById(final JobStatus status,
	                                      final Long jobDefinitionId,
	                                      final Long jobExecutionId) {
		final JobDefinition jobDefinition = getById(jobDefinitionId);
		
		final JobExecution jobExecution = jobDefinition.getExecutionById(jobExecutionId);
		jobExecution.setStatus(status);
		
		repository.save(jobDefinition);
	}
	
	@Transactional
	public void addTaskDefinitionsById(final List<TaskDefinition> taskDefinitions,
	                                   final Long jobDefinitionId) {
		final JobDefinition jobDefinition = getById(jobDefinitionId);
		
		final Set<URL> activeTaskDefinitionUrls = taskDefinitionService.getActiveTaskDefinitionUrlsByJobDefinitionId(jobDefinitionId);
		jobDefinition.addTaskDefinitions(
				taskDefinitions.stream()
						.filter(not(taskDefinition -> activeTaskDefinitionUrls.contains(taskDefinition.getUrl())))
						.toList()
		);
		
		save(jobDefinition);
	}
	
	@Transactional
	public List<TaskDefinition> getActiveTaskDefinitionsById(final Long jobDefinitionId) {
		return taskDefinitionService.getAllActiveByJobDefinitionId(jobDefinitionId);
	}
}
