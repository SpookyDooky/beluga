package com.x.scrape.scraping.worker.event;

import java.util.UUID;

public class WorkerFinishedEvent extends WorkerEvent {
	
	public WorkerFinishedEvent(final UUID jobId) {
		super(jobId);
	}
}
