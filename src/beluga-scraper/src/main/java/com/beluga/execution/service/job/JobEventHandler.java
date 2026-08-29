package com.beluga.execution.service.job;

import com.beluga.execution.event.job.JobEvent;
import com.beluga.execution.event.job.JobFinishedEvent;
import com.beluga.execution.event.job.JobStartedEvent;
import com.beluga.service.job.JobDefinitionService;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import static com.beluga.model.job_definition.JobStatus.ACTIVE;
import static com.beluga.model.job_definition.JobStatus.COMPLETED;

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

	@EventListener
	public void onJobFinishedEvent(final JobFinishedEvent event) {
		jobDefinitionService.setJobExecutionStatusById(COMPLETED, event.getJobDefinitionId(), event.getJobExecutionId());
	}
}
