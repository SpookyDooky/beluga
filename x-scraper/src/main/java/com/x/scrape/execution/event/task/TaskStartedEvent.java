package com.x.scrape.execution.event.task;

import com.x.scrape.execution.model.task.Task;

public class TaskStartedEvent extends TaskEvent {
	public TaskStartedEvent(final Task task) {
		super(task);
	}
}
