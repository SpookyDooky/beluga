package com.x.scrape.model.job_definition.configuration.execution_configuration;

import com.x.scrape.persistence.shared.model.HasId;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;

import static jakarta.persistence.GenerationType.IDENTITY;

@Entity
public class ExecutionConfiguration implements HasId {
	
	@Id
	@GeneratedValue(strategy = IDENTITY)
	private Long id;
	
	private int workers;
	private int tasksPerSecond;
	
	@Override
	public Long getId() {
		return id;
	}
	
	@Override
	public void setId(final Long id) {
		this.id = id;
	}
	
	public int getWorkers() {
		return workers;
	}
	
	public void setWorkers(final int workers) {
		this.workers = workers;
	}
	
	public int getTasksPerSecond() {
		return tasksPerSecond;
	}
	
	public void setTasksPerSecond(final int tasksPerSecond) {
		this.tasksPerSecond = tasksPerSecond;
	}
}
