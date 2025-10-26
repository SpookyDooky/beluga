package com.x.scrape.execution.service.worker.event;

import java.util.UUID;

public class WorkerStartedEvent extends WorkerEvent {
	
	public WorkerStartedEvent(final UUID jobId) {
		super(jobId);
	}
}
