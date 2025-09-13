package com.x.scrape.model.task.event;

import java.util.UUID;

public class TaskFailedEvent extends TaskEvent {
	
	public TaskFailedEvent(final UUID jobId,
	                       final UUID taskId) {
		super(jobId, taskId);
	}
}
