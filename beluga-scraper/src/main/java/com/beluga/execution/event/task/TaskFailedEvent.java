package com.x.scrape.execution.event.task;

import com.x.scrape.execution.model.task.Task;

public class TaskFailedEvent extends TaskEvent {
	
	public TaskFailedEvent(final Task task) {
		super(task);
	}
}
