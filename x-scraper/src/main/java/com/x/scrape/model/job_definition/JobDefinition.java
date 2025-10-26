package com.x.scrape.model.job_definition;

import com.x.scrape.model.job_definition.configuration.JobConfiguration;
import com.x.scrape.persistence.shared.model.HasId;

import java.util.ArrayList;
import java.util.List;

public class JobDefinition implements HasId {
	
	private Long id;
	private String name;
	
	private JobConfiguration jobConfiguration;
	private List<JobExecution> executions = new ArrayList<>();
	
	public JobDefinition(final JobConfiguration jobConfiguration) {
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
	
	public String getName() {
		return name;
	}
	
	public void setName(final String name) {
		this.name = name;
	}
	
	public JobConfiguration getJobConfiguration() {
		return jobConfiguration;
	}
	
	public void setJobConfiguration(final JobConfiguration jobConfiguration) {
		this.jobConfiguration = jobConfiguration;
	}
	
	public List<JobExecution> getExecutions() {
		return executions;
	}
	
	public void setExecutions(final List<JobExecution> executions) {
		this.executions = executions;
	}
	
	public void addExecution(final JobExecution jobExecution) {
		executions.add(jobExecution);
	}
}
