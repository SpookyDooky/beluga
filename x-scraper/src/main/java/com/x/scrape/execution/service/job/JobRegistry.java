package com.x.scrape.execution.service.job;

import com.x.scrape.execution.model.job.Job;
import com.x.scrape.logging.ContextLogger;
import com.x.scrape.mapper.job.JobDefinitionMapper;
import com.x.scrape.model.job_definition.JobDefinition;
import com.x.scrape.properties.XScraperProperties;
import com.x.scrape.properties.scraping.JobProperties;
import com.x.scrape.service.job.JobDefinitionService;
import com.x.scrape.service.job.JobService;
import com.x.scrape.util.TimingService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class JobRegistry {

	private final Map<Long, Job> jobRegistry = new ConcurrentHashMap<>();
	
	private final ContextLogger logger;
	private final XScraperProperties xScraperProperties;
	private final JobDefinitionMapper jobDefinitionMapper;
	private final JobExecutionService jobExecutionService;
	private final JobDefinitionService jobDefinitionService;
	private final JobService jobService;
	private final TimingService timingService;
	
	public JobRegistry(final ContextLogger logger,
	                   final XScraperProperties xScraperProperties,
	                   final JobDefinitionMapper jobDefinitionMapper,
	                   final JobExecutionService jobExecutionService,
	                   final JobDefinitionService jobDefinitionService,
	                   final JobService jobService,
	                   final TimingService timingService) {
		this.logger = logger;
		this.xScraperProperties = xScraperProperties;
		this.jobDefinitionMapper = jobDefinitionMapper;
		this.jobExecutionService = jobExecutionService;
		this.jobDefinitionService = jobDefinitionService;
		this.jobService = jobService;
		this.timingService = timingService;
	}
	
	@Scheduled(initialDelay = 0L)
	public void registerJobs() {
		if (xScraperProperties.getJobs() == null) {
			return;
		}
		
		xScraperProperties.getJobs()
				.forEach(this::registerConfigurationJob);
		
		jobRegistry.values()
				.forEach(jobExecutionService::executeJob);
	}
	
	private void registerConfigurationJob(final JobProperties jobProperties) {
		logger.info("Registering job");
		
		final UUID uuid = timingService.start();
		final JobDefinition jobDefinition = jobDefinitionMapper.map(jobProperties);
		jobDefinitionService.save(jobDefinition);
		
		final Job job = jobService.createJobByJobDefinitionId(jobDefinition.getId());
		logger.info("Saving job took: " + timingService.stop(uuid) + "ms");
		
		jobRegistry.put(job.getId(), job);
	}
	
	public Job get(final Long jobId) {
		return jobRegistry.get(jobId);
	}
}
