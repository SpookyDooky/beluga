package com.x.scrape.model.job_definition;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.x.scrape.model.job_definition.configuration.UrlConfiguration;
import com.x.scrape.model.job_definition.configuration.execution_configuration.ExecutionConfiguration;
import com.x.scrape.model.job_definition.configuration.scraping_configuration.ScrapingConfiguration;
import com.x.scrape.model.job_definition.configuration.storage_configuration.StorageConfiguration;
import com.x.scrape.model.job_definition.exception.TaskDefinitionNotFoundException;
import com.x.scrape.model.task.TaskDefinition;
import com.x.scrape.persistence.shared.model.HasId;
import jakarta.persistence.*;

import java.net.URL;
import java.util.*;
import java.util.stream.Collectors;

import static jakarta.persistence.CascadeType.ALL;
import static jakarta.persistence.GenerationType.IDENTITY;

@Entity
public class JobDefinition implements HasId {
	
	@Id
	@GeneratedValue(strategy = IDENTITY)
	private Long id;
	private String name;
	
	@OneToOne(cascade = ALL)
	@JoinColumn(name = "url_configuration_id")
	private UrlConfiguration urlConfiguration;
	
	@OneToOne(cascade = ALL)
	@JoinColumn(name = "scraping_configuration_id")
	private ScrapingConfiguration scrapingConfiguration;
	
	@OneToOne(cascade = ALL)
	@JoinColumn(name = "storage_configuration_id")
	private StorageConfiguration storageConfiguration;
	
	@OneToOne(cascade = ALL)
	@JoinColumn(name = "execution_configuration_id")
	private ExecutionConfiguration executionConfiguration;
	
	@OneToMany(
			cascade = ALL,
			mappedBy = "jobDefinition"
	)
	private List<TaskDefinition> taskDefinitions = new ArrayList<>();
	
	@OneToMany(
			cascade = ALL,
			mappedBy = "jobDefinition"
	)
	@OrderBy("executedAt DESC")
	private List<JobExecution> executions = new ArrayList<>();
	
	@Override
	public Long getId() {
		return id;
	}
	
	@Override
	public void setId(final Long id) {
		this.id = id;
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(final String name) {
		this.name = name;
	}
	
	public UrlConfiguration getUrlConfiguration() {
		return urlConfiguration;
	}
	
	public void setUrlConfiguration(final UrlConfiguration urlConfiguration) {
		this.urlConfiguration = urlConfiguration;
	}
	
	public ScrapingConfiguration getScrapingConfiguration() {
		return scrapingConfiguration;
	}
	
	public void setScrapingConfiguration(final ScrapingConfiguration scrapingConfiguration) {
		this.scrapingConfiguration = scrapingConfiguration;
	}
	
	public StorageConfiguration getStorageConfiguration() {
		return storageConfiguration;
	}
	
	public void setStorageConfiguration(final StorageConfiguration storageConfiguration) {
		this.storageConfiguration = storageConfiguration;
	}
	
	public ExecutionConfiguration getExecutionConfiguration() {
		return executionConfiguration;
	}
	
	public void setExecutionConfiguration(final ExecutionConfiguration executionConfiguration) {
		this.executionConfiguration = executionConfiguration;
	}
	
	public List<TaskDefinition> getTaskDefinitions() {
		return taskDefinitions;
	}
	
	public TaskDefinition getTaskDefinitionById(final Long taskDefinitionId) {
		return taskDefinitions.stream()
				.filter(taskDefinition -> taskDefinition.getId().equals(taskDefinitionId))
				.findFirst()
				.orElseThrow(TaskDefinitionNotFoundException::new);
	}
	
	public List<TaskDefinition> getActiveTaskDefinitions() {
		return taskDefinitions.stream()
				.filter(TaskDefinition::isActive)
				.collect(Collectors.toList());
	}
	
	public void setExistingTaskDefinitionsToInactive() {
		taskDefinitions.forEach(taskDefinition -> taskDefinition.setActive(false));
	}
	
	public void setTaskDefinitionsInactiveByUrl(final Collection<URL> urls) {
		final Set<URL> urlSet = new HashSet<>(urls);
		
		taskDefinitions.stream()
				.filter(taskDefinition -> urlSet.contains(taskDefinition.getUrl()))
				.forEach(taskDefinition -> taskDefinition.setActive(false));
	}
	
	public void setTaskDefinitions(final List<TaskDefinition> taskDefinitions) {
		this.taskDefinitions = taskDefinitions;
		taskDefinitions.forEach(taskDefinition -> taskDefinition.setJobDefinition(this));
	}
	
	public void addTaskDefinitions(final Collection<TaskDefinition> taskDefinitions) {
		taskDefinitions.forEach(this::addTaskDefinition);
	}
	
	private void addTaskDefinition(final TaskDefinition taskDefinition) {
		taskDefinition.setJobDefinition(this);
		taskDefinitions.add(taskDefinition);
	}
	
	public List<JobExecution> getExecutions() {
		return executions;
	}
	
	public void setExecutions(final List<JobExecution> executions) {
		this.executions = executions;
	}
	
	public void addExecution(final JobExecution jobExecution) {
		executions.add(jobExecution);
		jobExecution.setJobDefinition(this);
	}
	
	@JsonIgnore
	public Optional<JobExecution> getMostRecentExecution() {
		if (executions.isEmpty()) {
			return Optional.empty();
		}
		
		return Optional.of(executions.getFirst());
	}
	
	public Optional<JobExecution> findExecutionById(final Long executionId) {
		return executions.stream()
				.filter(execution -> executionId.equals(execution.getId()))
				.findFirst();
	}
	
	@JsonIgnore
	public JobExecution getExecutionById(final Long executionId) {
		return findExecutionById(executionId)
				.orElseThrow(() -> new EntityNotFoundException("Could not find execution with specified id " + executionId + "."));
	}
}
