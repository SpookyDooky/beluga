package com.x.scrape.api.execution.service;

import com.x.scrape.execution.model.Job;
import com.x.scrape.execution.service.job.JobExecutionService;
import com.x.scrape.service.job.JobDefinitionService;
import com.x.scrape.service.job.JobService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ExecutionApiService {
	
	private final JobDefinitionService jobDefinitionService;
	private final JobService jobService;
	private final JobExecutionService jobExecutionService;
	
	public ExecutionApiService(final JobDefinitionService jobDefinitionService,
	                           final JobService jobService,
	                           final JobExecutionService jobExecutionService) {
		this.jobDefinitionService = jobDefinitionService;
		this.jobService = jobService;
		this.jobExecutionService = jobExecutionService;
	}
	
	@Transactional
	public void start(final Long jobId) {
		final Job job = jobService.createJobByJobDefinitionId(jobId);
		
		jobExecutionService.executeJob(job);
	}
	
	@Transactional
	public void stop(final Long jobId) {
	
	}
	
	@Transactional
	public void pause(final Long jobId) {
	
	}
	
	@Transactional
	public void resume(final Long jobId) {
	
	}
}
