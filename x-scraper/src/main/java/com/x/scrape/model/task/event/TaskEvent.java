package com.x.scrape.model.task.event;

import org.springframework.context.ApplicationEvent;

import java.util.UUID;

public abstract class TaskEvent extends ApplicationEvent {
	
	private final UUID jobId;
	private final UUID taskId;
	
	public TaskEvent(final UUID jobId,
	                 final UUID taskId) {
		super(taskId);
		
		this.jobId = jobId;
		this.taskId = taskId;
	}
	
	public UUID getJobId() {
		return jobId;
	}
	
	public UUID getTaskId() {
		return taskId;
	}
}
