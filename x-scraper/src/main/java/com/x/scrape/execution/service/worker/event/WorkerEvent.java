package com.x.scrape.execution.service.worker.event;

import org.springframework.context.ApplicationEvent;

import java.util.UUID;

public abstract class WorkerEvent extends ApplicationEvent {
	
	private final UUID jobId;
	
	public WorkerEvent(final UUID jobId) {
		super(jobId);
		this.jobId = jobId;
	}
	
	public UUID getJobId() {
		return jobId;
	}
}
