package com.x.scrape.execution.service.worker.event;

public class JobWorkersFinishedEvent extends WorkerEvent {
	
	public JobWorkersFinishedEvent(final Long jobId) {
		super(jobId);
	}
}
