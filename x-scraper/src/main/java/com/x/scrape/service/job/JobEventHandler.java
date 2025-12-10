package com.x.scrape.service.job;

import com.x.scrape.execution.event.job.JobEvent;
import com.x.scrape.execution.event.job.JobStartedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import static com.x.scrape.model.job_definition.JobStatus.ACTIVE;

/**
 * Handles {@link JobEvent}'s.
 */
@Component
public class JobEventHandler {
	
	private final JobDefinitionService jobDefinitionService;
	
	public JobEventHandler(final JobDefinitionService jobDefinitionService) {
		this.jobDefinitionService = jobDefinitionService;
	}
	
	@EventListener
	public void onJobStartedEvent(final JobStartedEvent jobStartedEvent) {
		jobDefinitionService.setJobExecutionStatusById(ACTIVE, jobStartedEvent.getJobDefinitionId(), jobStartedEvent.getJobExecutionId());
	}
}
