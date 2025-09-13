package com.x.scrape.model.task.event;

import java.util.List;
import java.util.Map;
import java.util.UUID;

public class TaskCompletedEvent extends TaskEvent {
	
	private final List<Map<String, Object>> result;
	
	public TaskCompletedEvent(final UUID jobId,
	                          final UUID taskId,
	                          final List<Map<String, Object>> result) {
		super(jobId, taskId);
		this.result = result;
	}
	
	public List<Map<String, Object>> getResult() {
		return result;
	}
}
