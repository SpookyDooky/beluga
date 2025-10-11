package com.x.scrape.model.task.event;

import com.x.scrape.logging.ContextLoggable;
import org.springframework.context.ApplicationEvent;

import java.util.Map;
import java.util.UUID;

import static com.x.scrape.logging.ContextKeys.JOB_ID;
import static com.x.scrape.logging.ContextKeys.TASK_ID;

public abstract class TaskEvent extends ApplicationEvent implements ContextLoggable {
	
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
	
	@Override
	public Map<String, String> loggingContext() {
		return Map.of(
				JOB_ID, jobId.toString(),
				TASK_ID, taskId.toString()
		);
	}
}
