package com.beluga.api.execution.dto;

import java.util.ArrayList;
import java.util.List;

public class ReadJobExecutionWithTasksDto extends ReadJobExecutionDto {
	
	private List<ReadTaskExecutionDto> tasks = new ArrayList<>();
	
	public List<ReadTaskExecutionDto> getTasks() {
		return tasks;
	}
	
	public void setTasks(final List<ReadTaskExecutionDto> tasks) {
		this.tasks = tasks;
	}
}
