package com.x.scrape.execution.service.worker.event;

public class WorkerFinishedEvent extends WorkerEvent {
	
	public WorkerFinishedEvent(final Long jobId) {
		super(jobId);
	}
}
