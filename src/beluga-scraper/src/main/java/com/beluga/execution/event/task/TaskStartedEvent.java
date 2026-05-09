package com.beluga.execution.event.task;

import com.beluga.execution.model.task.Task;

public class TaskStartedEvent extends TaskEvent {
	public TaskStartedEvent(final Task task) {
		super(task);
	}
}
