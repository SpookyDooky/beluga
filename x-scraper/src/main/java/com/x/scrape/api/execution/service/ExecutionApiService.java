package com.x.scrape.api.execution.service;

import com.x.scrape.execution.model.Job;
import com.x.scrape.execution.service.job.JobExecutionService;
import com.x.scrape.model.job_definition.JobDefinition;
import com.x.scrape.model.job_definition.JobExecution;
import com.x.scrape.service.job.JobDefinitionService;
import com.x.scrape.service.job.JobService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import java.util.Optional;

import static com.x.scrape.model.job_definition.JobStatus.STOPPED;

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
		
		start(job);
	}
	
	private void start(final Job job) {
		TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronization() {
			@Override
			public void afterCommit() {
				jobExecutionService.executeJob(job);
			}
		});
	}
	
	@Transactional
	public void stop(final Long jobId) {
		final JobDefinition jobDefinition = jobDefinitionService.getById(jobId);
		final Optional<JobExecution> latestExecutionOptional = jobDefinition.getMostRecentExecution();
		
		if (latestExecutionOptional.isPresent()) {
			final JobExecution latestExecution = latestExecutionOptional.get();
			jobDefinitionService.setJobExecutionStatusById(STOPPED, jobId, latestExecution.getId());
		}
	}
	
	@Transactional
	public void pause(final Long jobId) {
	
	}
	
	@Transactional
	public void resume(final Long jobId) {
	
	}
}
