package com.x.scrape.model.task.event;

import com.x.scrape.logging.ContextLoggable;
import com.x.scrape.model.task.Task;
import org.springframework.context.ApplicationEvent;

import java.util.Map;
import java.util.UUID;

import static com.x.scrape.logging.ContextKeys.*;

public abstract class TaskEvent extends ApplicationEvent implements ContextLoggable {
	
	private final Long jobId;
	private final UUID taskId;
	
	public TaskEvent(final Task task) {
		super(task.getUuid());
		
		this.jobId = task.getJob().getId();
		this.taskId = task.getUuid();
	}
	
	public Long getJobId() {
		return jobId;
	}
	
	public UUID getTaskId() {
		return taskId;
	}
	
	@Override
	public Map<String, String> loggingContext() {
		return Map.of(
				JOB_EXECUTION_ID, jobId.toString(),
				TASK_ID, taskId.toString()
		);
	}
}
