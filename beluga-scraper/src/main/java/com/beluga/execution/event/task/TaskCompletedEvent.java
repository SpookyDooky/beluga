package com.x.scrape.execution.event.task;

import com.x.scrape.execution.model.task.Task;

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
