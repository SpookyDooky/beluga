package com.x.scrape.execution.model.job;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.x.scrape.model.job_definition.JobDefinition;
import com.x.scrape.model.job_definition.JobExecution;
import com.x.scrape.execution.model.task.StorageConfiguration;
import com.x.scrape.execution.model.task.Task;

import java.util.ArrayList;
import java.util.List;

/**
 * This is aimed at the execution of a {@link JobDefinition}.
 */
public class Job {
	
	/**
	 * This is the id of the {@link JobExecution}.
	 */
	private Long id;
	
	private Long jobDefinitionId;
	private String jobName;
	
	private StorageConfiguration storageConfiguration;
	private ExecutionConfiguration executionConfiguration;
	
	private List<Task> tasks = new ArrayList<>();
	
	public Long getId() {
		return id;
	}
	
	public void setId(final Long id) {
		this.id = id;
	}
	
	public Long getJobDefinitionId() {
		return jobDefinitionId;
	}
	
	public void setJobDefinitionId(final Long jobDefinitionId) {
		this.jobDefinitionId = jobDefinitionId;
	}
	
	public String getJobName() {
		return jobName;
	}
	
	public void setJobName(final String jobName) {
		this.jobName = jobName;
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
	
	@JsonIgnore
	public String getJobFolder() {
		return storageConfiguration.getFolder() + "/"
				+ jobName;
	}
	
	@JsonIgnore
	public String getJobTaskResultsFolder() {
		return getJobFolder() + "/job-executions/" + id + "/results/tasks";
	}
	
	public List<Task> getTasks() {
		return tasks;
	}
	
	public void setTasks(final List<Task> tasks) {
		this.tasks = tasks;
	}
}
