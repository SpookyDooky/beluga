package com.x.scrape.model.job_definition;

import com.x.scrape.execution.model.task.TaskDefinition;
import com.x.scrape.model.job_definition.configuration.UrlConfiguration;
import com.x.scrape.model.job_definition.configuration.execution_configuration.ExecutionDefinition;
import com.x.scrape.model.job_definition.configuration.scraping_configuration.ScrapingDefinition;
import com.x.scrape.model.job_definition.configuration.storage_configuration.StorageDefinition;
import com.x.scrape.model.job_definition.exception.TaskDefinitionNotFoundException;
import jakarta.persistence.*;

import java.net.URL;
import java.util.*;
import java.util.stream.Collectors;

import static jakarta.persistence.CascadeType.ALL;
import static jakarta.persistence.GenerationType.IDENTITY;

@Entity
public class JobDefinition {
	
	@Id
	@GeneratedValue(strategy = IDENTITY)
	private Long id;
	private String name;
	
	@OneToOne(cascade = ALL)
	@JoinColumn(name = "url_configuration_id")
	private UrlConfiguration urlConfiguration;
	
	@OneToOne(cascade = ALL)
	@JoinColumn(name = "scraping_definition_id")
	private ScrapingDefinition scrapingDefinition;
	
	@OneToOne(cascade = ALL)
	@JoinColumn(name = "storage_definition_id")
	private StorageDefinition storageDefinition;
	
	@OneToOne(cascade = ALL)
	@JoinColumn(name = "execution_definition_id")
	private ExecutionDefinition executionDefinition;
	
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
	
	public Long getId() {
		return id;
	}
	
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
	
	public ScrapingDefinition getScrapingDefinition() {
		return scrapingDefinition;
	}
	
	public void setScrapingDefinition(final ScrapingDefinition scrapingDefinition) {
		this.scrapingDefinition = scrapingDefinition;
	}
	
	public StorageDefinition getStorageDefinition() {
		return storageDefinition;
	}
	
	public void setStorageDefinition(final StorageDefinition storageDefinition) {
		this.storageDefinition = storageDefinition;
	}
	
	public ExecutionDefinition getExecutionDefinition() {
		return executionDefinition;
	}
	
	public void setExecutionDefinition(final ExecutionDefinition executionDefinition) {
		this.executionDefinition = executionDefinition;
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
	
	public JobExecution getExecutionById(final Long executionId) {
		return findExecutionById(executionId)
				.orElseThrow(() -> new EntityNotFoundException("Could not find execution with specified id " + executionId + "."));
	}
	
	public boolean hasExecutionById(final Long executionId) {
		return findExecutionById(executionId)
				.isPresent();
	}
}
