package com.x.scrape.scraping.job;

import com.x.scrape.mapper.job.JobMapper;
import com.x.scrape.model.job.Job;
import com.x.scrape.properties.XScraperProperties;
import com.x.scrape.properties.scraping.JobProperties;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class JobRegistry {

	private final Logger logger = LogManager.getLogger();
	private final Map<UUID, Job> jobRegistry = new ConcurrentHashMap<>();
	
	private final XScraperProperties xScraperProperties;
	private final JobMapper jobMapper;
	private final JobExecutionService jobExecutionService;
	
	public JobRegistry(final XScraperProperties xScraperProperties,
	                   final JobMapper jobMapper,
	                   final JobExecutionService jobExecutionService) {
		this.xScraperProperties = xScraperProperties;
		this.jobMapper = jobMapper;
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
		
		final Job job = jobMapper.map(jobProperties);
		jobRegistry.put(job.getId(), job);
	}
	
	public Job get(final UUID jobId) {
		return jobRegistry.get(jobId);
	}
}
