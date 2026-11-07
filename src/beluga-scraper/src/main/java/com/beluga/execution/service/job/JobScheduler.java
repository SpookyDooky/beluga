package com.beluga.execution.service.job;

import com.beluga.execution.model.job.Job;
import com.beluga.service.job.JobExecutionService;
import com.beluga.service.job.JobService;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.beluga.model.job_definition.JobStatus.ACTIVE;
import static com.beluga.model.job_definition.JobStatus.PLANNED;

/**
 * Schedules the {@link Job}'s that need to run when the application starts.
 */
@Service
public class JobScheduler {

	private final JobExecutionService jobExecutionService;
	private final JobService jobService;
	private final JobExecutorService jobExecutorService;

	public JobScheduler(final JobExecutionService jobExecutionService,
	                    final JobService jobService,
	                    final JobExecutorService jobExecutorService) {
		this.jobExecutionService = jobExecutionService;
		this.jobService = jobService;
		this.jobExecutorService = jobExecutorService;
	}

	/**
	 * Continues the {@link Job}'s that had not finished when the application stopped.
	 */
	@Async
	@EventListener(ApplicationReadyEvent.class)
	@Transactional
	public void startOutstandingJobs() {
		jobExecutionService.findAllByStatus(List.of(PLANNED, ACTIVE)).stream()
				.map(jobService::createActiveJob)
				.forEach(jobExecutorService::execute);
	}
}
