package com.x.scrape.model.task.event;

import com.x.scrape.model.task.Task;

public class TaskCompletedEvent extends TaskEvent {
	
	public TaskCompletedEvent(final Task task) {
		super(task);
	}
}
