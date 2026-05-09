package com.beluga.execution.event.task;

import com.beluga.execution.model.task.Task;

public class TaskFailedEvent extends TaskEvent {
	
	public TaskFailedEvent(final Task task) {
		super(task);
	}
}
