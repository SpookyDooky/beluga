package com.beluga.api.results.dto;

import java.util.List;

public class TaskResultDto {
	
	private Long taskId;
	private List<Object> data;
	
	public Long getTaskId() {
		return taskId;
	}
	
	public void setTaskId(final Long taskId) {
		this.taskId = taskId;
	}
	
	public List<Object> getData() {
		return data;
	}
	
	public void setData(final List<Object> data) {
		this.data = data;
	}
}
