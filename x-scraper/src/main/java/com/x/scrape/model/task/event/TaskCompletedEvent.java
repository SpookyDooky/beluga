package com.x.scrape.model.task.event;

import java.util.UUID;

public class TaskCompletedEvent extends TaskEvent {
	
	public TaskCompletedEvent(final UUID jobId,
	                          final UUID taskId) {
		super(jobId, taskId);
	}
}
