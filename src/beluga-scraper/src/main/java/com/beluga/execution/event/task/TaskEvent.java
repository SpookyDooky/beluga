package com.beluga.execution.event.task;

import com.beluga.execution.model.task.Task;
import com.beluga.logging.ContextLoggable;
import org.springframework.context.ApplicationEvent;

import java.util.Map;

import static com.beluga.logging.ContextKeys.JOB_EXECUTION_ID;
import static com.beluga.logging.ContextKeys.TASK_ID;

public abstract class TaskEvent extends ApplicationEvent implements ContextLoggable {

	private final Long jobDefinitionId;
	private final Long jobId;
	private final Long taskId;
	
	public TaskEvent(final Task task) {
		this(
				task.getJob().getJobDefinitionId(),
				task.getJob().getId(),
				task.getId()
		);
	}

	public TaskEvent(final Long jobDefinitionId,
					 final Long jobId,
					 final Long taskId) {
		super(taskId);
		this.jobDefinitionId = jobDefinitionId;
		this.jobId = jobId;
		this.taskId = taskId;
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
