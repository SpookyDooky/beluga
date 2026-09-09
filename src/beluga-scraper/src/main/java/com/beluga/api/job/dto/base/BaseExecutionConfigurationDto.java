package com.beluga.api.job.dto.base;

import jakarta.validation.constraints.Positive;

public abstract class BaseExecutionConfigurationDto {

	@Positive
	private Double tasksPerSecond;
	
	public Double getTasksPerSecond() {
		return tasksPerSecond;
	}
	
	public void setTasksPerSecond(final Double tasksPerSecond) {
		this.tasksPerSecond = tasksPerSecond;
	}
}
