package com.beluga.model.job_definition.configuration.execution_configuration;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;

import static jakarta.persistence.GenerationType.IDENTITY;

@Entity
public class ExecutionDefinition {
	
	@Id
	@GeneratedValue(strategy = IDENTITY)
	private Long id;

	private double tasksPerSecond;
	
	public Long getId() {
		return id;
	}
	
	public void setId(final Long id) {
		this.id = id;
	}
	
	public double getTasksPerSecond() {
		return tasksPerSecond;
	}
	
	public void setTasksPerSecond(final double tasksPerSecond) {
		this.tasksPerSecond = tasksPerSecond;
	}
}
