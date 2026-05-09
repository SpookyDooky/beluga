package com.beluga.execution.event.job;

public class JobFinishedEvent extends JobEvent {
	
	private final Long jobExecutionId;
	
	public JobFinishedEvent(final Long jobDefinitionId,
	                        final Long jobExecutionId) {
		super(jobDefinitionId);
		this.jobExecutionId = jobExecutionId;
	}
	
	public Long getJobExecutionId() {
		return jobExecutionId;
	}
}
