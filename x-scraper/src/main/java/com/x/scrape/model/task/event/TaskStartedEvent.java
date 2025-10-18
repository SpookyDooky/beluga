package com.x.scrape.model.task.event;

import java.util.UUID;

public class TaskStartedEvent extends TaskEvent {
	public TaskStartedEvent(final UUID jobId,
	                        final UUID taskId) {
		super(jobId, taskId);
	}
}
