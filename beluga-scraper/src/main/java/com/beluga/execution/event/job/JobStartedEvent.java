package com.beluga.execution.event.job;

public class JobStartedEvent extends JobEvent {
	
	private final Long jobExecutionId;
	
	public JobStartedEvent(final Long jobDefinitionId,
	                       final Long jobExecutionId) {
		super(jobDefinitionId);
		this.jobExecutionId = jobExecutionId;
	}
	
	public Long getJobExecutionId() {
		return jobExecutionId;
	}
}
