package com.x.scrape.api.job.dto.base;

import jakarta.validation.constraints.Positive;

public abstract class BaseExecutionConfigurationDto {
	
	@Positive
	private Integer workers;
	@Positive
	private Integer tasksPerSecond;
	
	public Integer getWorkers() {
		return workers;
	}
	
	public void setWorkers(final Integer workers) {
		this.workers = workers;
	}
	
	public Integer getTasksPerSecond() {
		return tasksPerSecond;
	}
	
	public void setTasksPerSecond(final Integer tasksPerSecond) {
		this.tasksPerSecond = tasksPerSecond;
	}
}
