package com.x.scrape.execution.service.worker.event;

public class WorkerStartedEvent extends WorkerEvent {
	
	public WorkerStartedEvent(final Long jobId) {
		super(jobId);
	}
}
