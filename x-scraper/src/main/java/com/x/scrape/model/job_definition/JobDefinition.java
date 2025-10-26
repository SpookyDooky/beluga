package com.x.scrape.model.job_definition;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.x.scrape.model.job_definition.configuration.JobConfiguration;
import com.x.scrape.persistence.shared.model.HasId;

import java.io.File;
import java.util.List;
import java.util.UUID;

public class JobDefinition implements HasId {
	
	private Long id;
	private final UUID uuid;
	private final JobConfiguration jobConfiguration;
	private List<JobExecution> executions;
	
	public JobDefinition(final JobConfiguration jobConfiguration) {
		uuid = UUID.randomUUID();
		this.jobConfiguration = jobConfiguration;
	}
	
	@Override
	public Long getId() {
		return id;
	}
	
	@Override
	public void setId(final Long id) {
		this.id = id;
	}
	
	public UUID getUuid() {
		return uuid;
	}
	
	public JobConfiguration getJobConfiguration() {
		return jobConfiguration;
	}
	
	public List<JobExecution> getExecutions() {
		return executions;
	}
	
	public void setExecutions(final List<JobExecution> executions) {
		this.executions = executions;
	}
	
	@JsonIgnore
	public void createJobFolders() {
		final File file = new File(getJobTaskResultsFolder());
		file.mkdirs();
	}
	
	@JsonIgnore
	public String getJobFolder() {
		return jobConfiguration.getStorageConfiguration().getFolder()
				+ jobConfiguration.getName();
	}
	
	@JsonIgnore
	public String getJobTaskResultsFolder() {
		return getJobFolder() + "/job-executions/" + uuid + "/results/tasks";
	}
}
