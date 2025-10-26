package com.x.scrape.execution.service.worker.event;

import java.util.UUID;

public class WorkerFinishedEvent extends WorkerEvent {
	
	public WorkerFinishedEvent(final UUID jobId) {
		super(jobId);
	}
}
