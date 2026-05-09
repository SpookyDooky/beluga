package com.beluga.execution.event.job;

import org.springframework.context.ApplicationEvent;

public abstract class JobEvent extends ApplicationEvent {
	
	private final Long jobDefinitionId;
	
	public JobEvent(final Long jobDefinitionId) {
		super(jobDefinitionId);
		this.jobDefinitionId = jobDefinitionId;
	}
	
	public Long getJobDefinitionId() {
		return jobDefinitionId;
	}
}
