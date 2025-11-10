package com.x.scrape.model.task.event;

import com.x.scrape.model.task.Task;

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
