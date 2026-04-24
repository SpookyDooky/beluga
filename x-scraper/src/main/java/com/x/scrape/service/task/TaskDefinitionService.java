package com.x.scrape.service.task;

import com.x.scrape.execution.model.task.TaskDefinition;
import com.x.scrape.persistence.repository.TaskDefinitionRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static org.springframework.transaction.annotation.Propagation.MANDATORY;

@Service
public class TaskDefinitionService {

	private final TaskDefinitionRepository taskDefinitionRepository;
	
	public TaskDefinitionService(final TaskDefinitionRepository taskDefinitionRepository) {
		this.taskDefinitionRepository = taskDefinitionRepository;
	}
	
	@Transactional
	public Set<URL> getActiveTaskDefinitionUrlsByJobDefinitionId(final Long jobDefinitionId) {
		return taskDefinitionRepository.findAllActiveTaskDefinitionUrlsByJobDefinitionId(jobDefinitionId)
				.stream()
				.map(URI::create)
				.map(uri -> {
					try {
						return uri.toURL();
					} catch (final MalformedURLException e) {
						throw new IllegalArgumentException(e);
					}
				})
				.collect(Collectors.toSet());
	}
	
	@Transactional(propagation = MANDATORY)
	public List<TaskDefinition> getAllActiveByJobDefinitionId(final Long jobDefinitionId) {
		return taskDefinitionRepository.findAllByActiveAndJobDefinitionId(jobDefinitionId);
	}
}
