package com.beluga.execution.event.task;

import com.beluga.logging.ContextLoggable;
import com.beluga.execution.model.task.Task;
import org.springframework.context.ApplicationEvent;

import java.util.Map;

import static com.beluga.logging.ContextKeys.JOB_EXECUTION_ID;
import static com.beluga.logging.ContextKeys.TASK_ID;

public abstract class TaskEvent extends ApplicationEvent implements ContextLoggable {

	private final Long jobDefinitionId;
	private final Long jobId;
	private final Long taskId;
	
	public TaskEvent(final Task task) {
		super(task.getId());
		this.jobDefinitionId = task.getJob().getJobDefinitionId();
		this.jobId = task.getJob().getId();
		this.taskId = task.getId();
	}

	public Long getJobDefinitionId() {
		return jobDefinitionId;
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
