package com.x.scrape.api.execution.service;

import com.x.scrape.api.execution.dto.ReadJobExecutionDto;
import com.x.scrape.api.execution.dto.ReadJobExecutionWithTasksDto;
import com.x.scrape.api.execution.mapper.ReadJobExecutionMapper;
import com.x.scrape.execution.model.job.Job;
import com.x.scrape.execution.service.job.JobExecutionService;
import com.x.scrape.model.job_definition.JobDefinition;
import com.x.scrape.model.job_definition.JobExecution;
import com.x.scrape.service.job.JobDefinitionService;
import com.x.scrape.service.job.JobService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import java.util.List;
import java.util.Optional;

import static com.x.scrape.model.job_definition.JobStatus.*;

// Todo - this entire service should support multiple running jobs and executions should be stopped/resumed/paused based on the job execution id.
@Service
public class ExecutionApiService {
	
	private final JobDefinitionService jobDefinitionService;
	private final JobService jobService;
	private final JobExecutionService jobExecutionService;
	private final ReadJobExecutionMapper readJobExecutionMapper;
	
	public ExecutionApiService(final JobDefinitionService jobDefinitionService,
	                           final JobService jobService,
	                           final JobExecutionService jobExecutionService,
	                           final ReadJobExecutionMapper readJobExecutionMapper) {
		this.jobDefinitionService = jobDefinitionService;
		this.jobService = jobService;
		this.jobExecutionService = jobExecutionService;
		this.readJobExecutionMapper = readJobExecutionMapper;
	}
	
	@Transactional
	public void start(final Long jobDefinitionId) {
		final Job job = jobService.createJobByJobDefinitionId(jobDefinitionId);
		
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
	public void stop(final Long jobDefinitionId) {
		final JobDefinition jobDefinition = jobDefinitionService.getById(jobDefinitionId);
		final Optional<JobExecution> latestExecutionOptional = jobDefinition.getMostRecentExecution();
		
		if (latestExecutionOptional.isPresent()) {
			final JobExecution latestExecution = latestExecutionOptional.get();
			
			// Todo - reconsider when a job can be stopped
			if (latestExecution.getStatus() == COMPLETED) {
				return;
			}
			
			jobDefinitionService.setJobExecutionStatusById(STOPPED, jobDefinitionId, latestExecution.getId());
			jobExecutionService.stop(latestExecution.getId());
		}
	}
	
	@Transactional
	public void pause(final Long jobDefinitionId) {
		final JobDefinition jobDefinition = jobDefinitionService.getById(jobDefinitionId);
		final Optional<JobExecution> latestExecutionOptional = jobDefinition.getMostRecentExecution();
		
		if (latestExecutionOptional.isPresent()) {
			final JobExecution latestExecution = latestExecutionOptional.get();
			
			// Todo - reconsider when a job can be paused
			if (latestExecution.getStatus() == COMPLETED) {
				return;
			}
			
			jobDefinitionService.setJobExecutionStatusById(PAUSED, jobDefinitionId, latestExecution.getId());
			jobExecutionService.pause(latestExecution.getId());
		}
	}
	
	@Transactional
	public void resume(final Long jobDefinitionId) {
		final JobDefinition jobDefinition = jobDefinitionService.getById(jobDefinitionId);
		final Optional<JobExecution> latestExecutionOptional = jobDefinition.getMostRecentExecution();
		
		if (latestExecutionOptional.isPresent()) {
			final JobExecution latestExecution = latestExecutionOptional.get();
			
			if (latestExecution.getStatus() != PAUSED) {
				return;
			}
			
			jobDefinitionService.setJobExecutionStatusById(ACTIVE, jobDefinitionId, latestExecution.getId());
			final Optional<Job> jobOptional = jobService.createResumedJob(jobDefinitionId);
			
			if (jobOptional.isPresent()) {
				final Job job = jobOptional.get();
				jobExecutionService.executeJob(job);
			}
		}
	}
	
	@Transactional
	public Optional<ReadJobExecutionDto> getLatestJobExecution(final Long jobDefinitionId) {
		final JobDefinition jobDefinition = jobDefinitionService.getById(jobDefinitionId);
		
		return jobDefinition.getMostRecentExecution()
				.map(readJobExecutionMapper::map);
	}
	
	@Transactional
	public List<ReadJobExecutionDto> getExecutions(final Long jobDefinitionId) {
		final JobDefinition jobDefinition = jobDefinitionService.getById(jobDefinitionId);
		
		return jobDefinition.getExecutions().stream()
				.map(readJobExecutionMapper::map)
				.toList();
	}
	
	@Transactional
	public Optional<ReadJobExecutionWithTasksDto> getExecution(final Long jobDefinitionId,
	                                                           final Long jobExecutionId) {
		final JobDefinition jobDefinition = jobDefinitionService.getById(jobDefinitionId);
		
		return jobDefinition.findExecutionById(jobExecutionId)
				.map(readJobExecutionMapper::mapWithTasks);
	}
}
