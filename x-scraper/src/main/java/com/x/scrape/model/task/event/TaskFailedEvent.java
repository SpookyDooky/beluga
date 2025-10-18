package com.x.scrape.model.task.event;

import com.x.scrape.model.task.Task;

public class TaskFailedEvent extends TaskEvent {
	
	public TaskFailedEvent(final Task task) {
		super(task);
	}
}
