package com.beluga.execution.service.worker.event;

import org.springframework.context.ApplicationEvent;

public abstract class WorkerEvent extends ApplicationEvent {
	
	private final Long jobId;
	
	public WorkerEvent(final Long jobId) {
		super(jobId);
		this.jobId = jobId;
	}
	
	public Long getJobId() {
		return jobId;
	}
}
