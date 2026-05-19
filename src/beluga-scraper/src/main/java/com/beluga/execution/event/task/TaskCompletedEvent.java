package com.beluga.execution.event.task;

import com.beluga.execution.model.task.Task;

public class TaskCompletedEvent extends TaskEvent {
	
	public TaskCompletedEvent(final Task task) {
		super(task);
	}
}
