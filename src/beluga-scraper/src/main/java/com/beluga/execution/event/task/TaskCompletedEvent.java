package com.beluga.execution.event.task;

import com.beluga.execution.model.task.Task;

public class TaskCompletedEvent extends TaskEvent {
	
	private final String resultFolder;
	
	public TaskCompletedEvent(final Task task,
	                          final String resultFolder) {
		super(task);
		this.resultFolder = resultFolder;
	}
	
	public String getResultFolder() {
		return resultFolder;
	}
}
