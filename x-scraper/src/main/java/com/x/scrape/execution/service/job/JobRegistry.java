package com.x.scrape.execution.service.job;

import com.x.scrape.logging.ContextLogger;
import com.x.scrape.mapper.job.JobConfigurationMapper;
import com.x.scrape.model.job_definition.JobDefinition;
import com.x.scrape.properties.XScraperProperties;
import com.x.scrape.properties.scraping.JobProperties;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class JobRegistry {

	private final Map<UUID, JobDefinition> jobRegistry = new ConcurrentHashMap<>();
	
	private final ContextLogger logger;
	private final XScraperProperties xScraperProperties;
	private final JobConfigurationMapper jobConfigurationMapper;
	private final JobExecutionService jobExecutionService;
	
	public JobRegistry(final ContextLogger logger,
	                   final XScraperProperties xScraperProperties,
	                   final JobConfigurationMapper jobConfigurationMapper,
	                   final JobExecutionService jobExecutionService) {
		this.logger = logger;
		this.xScraperProperties = xScraperProperties;
		this.jobConfigurationMapper = jobConfigurationMapper;
		this.jobExecutionService = jobExecutionService;
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
		
		final JobDefinition jobDefinition = jobConfigurationMapper.map(jobProperties)
						.getJob();
		jobRegistry.put(jobDefinition.getUuid(), jobDefinition);
	}
	
	public JobDefinition get(final UUID jobId) {
		return jobRegistry.get(jobId);
	}
}
