package com.x.scrape.execution.event.task;

import com.x.scrape.logging.ContextLoggable;
import com.x.scrape.execution.model.task.Task;
import org.springframework.context.ApplicationEvent;

import java.util.Map;

import static com.x.scrape.logging.ContextKeys.JOB_EXECUTION_ID;
import static com.x.scrape.logging.ContextKeys.TASK_ID;

public abstract class TaskEvent extends ApplicationEvent implements ContextLoggable {
	
	private final Long jobId;
	private final Long taskId;
	
	public TaskEvent(final Task task) {
		super(task.getId());
		
		this.jobId = task.getJob().getId();
		this.taskId = task.getId();
	}
	
	public Long getJobId() {
		return jobId;
	}
	
	public Long getTaskId() {
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
