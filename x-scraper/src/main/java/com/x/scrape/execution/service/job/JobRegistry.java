package com.x.scrape.execution.service.job;

import com.x.scrape.execution.model.Job;
import com.x.scrape.logging.ContextLogger;
import com.x.scrape.mapper.job.JobConfigurationMapper;
import com.x.scrape.model.job_definition.JobDefinition;
import com.x.scrape.properties.XScraperProperties;
import com.x.scrape.properties.scraping.JobProperties;
import com.x.scrape.service.JobDefinitionService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class JobRegistry {

	private final Map<Long, Job> jobRegistry = new ConcurrentHashMap<>();
	
	private final ContextLogger logger;
	private final XScraperProperties xScraperProperties;
	private final JobConfigurationMapper jobConfigurationMapper;
	private final JobExecutionService jobExecutionService;
	private final JobDefinitionService jobDefinitionService;
	
	public JobRegistry(final ContextLogger logger,
	                   final XScraperProperties xScraperProperties,
	                   final JobConfigurationMapper jobConfigurationMapper,
	                   final JobExecutionService jobExecutionService,
	                   final JobDefinitionService jobDefinitionService) { // Needs to use the job service
		this.logger = logger;
		this.xScraperProperties = xScraperProperties;
		this.jobConfigurationMapper = jobConfigurationMapper;
		this.jobExecutionService = jobExecutionService;
		this.jobDefinitionService = jobDefinitionService;
	}
	
	@Scheduled(initialDelay = 0L)
	public void registerJobs() {
		xScraperProperties.getJobs()
				.forEach(this::registerConfigurationJob);
		
		jobRegistry.values()
				.forEach(jobExecutionService::executeJob);
	}
	
	private void registerConfigurationJob(final JobProperties jobProperties) {
		logger.info("Registering job");
		
		final JobDefinition jobDefinition = jobConfigurationMapper.map(jobProperties);
		jobDefinitionService.save(jobDefinition);
		
		final Job job = jobDefinitionService.createJob(jobDefinition);
		jobRegistry.put(job.getId(), job);
	}
	
	public Job get(final Long jobId) {
		return jobRegistry.get(jobId);
	}
}
