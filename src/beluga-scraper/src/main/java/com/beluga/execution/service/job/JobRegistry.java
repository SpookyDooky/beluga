package com.beluga.execution.service.job;

import com.beluga.execution.model.job.Job;
import com.beluga.logging.ContextLogger;
import com.beluga.mapper.job.JobDefinitionMapper;
import com.beluga.model.job_definition.JobDefinition;
import com.beluga.properties.BelugaScraperProperties;
import com.beluga.properties.scraping.JobProperties;
import com.beluga.service.job.JobDefinitionService;
import com.beluga.service.job.JobService;
import com.beluga.util.TimingService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class JobRegistry {

	private final Map<Long, Job> jobRegistry = new ConcurrentHashMap<>();
	
	private final ContextLogger logger;
	private final BelugaScraperProperties belugaScraperProperties;
	private final JobDefinitionMapper jobDefinitionMapper;
	private final JobExecutionService jobExecutionService;
	private final JobDefinitionService jobDefinitionService;
	private final JobService jobService;
	private final TimingService timingService;
	
	public JobRegistry(final ContextLogger logger,
	                   final BelugaScraperProperties belugaScraperProperties,
	                   final JobDefinitionMapper jobDefinitionMapper,
	                   final JobExecutionService jobExecutionService,
	                   final JobDefinitionService jobDefinitionService,
	                   final JobService jobService,
	                   final TimingService timingService) {
		this.logger = logger;
		this.belugaScraperProperties = belugaScraperProperties;
		this.jobDefinitionMapper = jobDefinitionMapper;
		this.jobExecutionService = jobExecutionService;
		this.jobDefinitionService = jobDefinitionService;
		this.jobService = jobService;
		this.timingService = timingService;
	}

	/**
	 * This method only registers {@link JobDefinition}'s that are configured through {@link BelugaScraperProperties}
	 */
	@Scheduled(initialDelay = 0L)
	public void registerJobs() {
		if (belugaScraperProperties.getJobs() == null) {
			return;
		}
		
		belugaScraperProperties.getJobs()
				.forEach(this::registerConfigurationJob);
		
		jobRegistry.values()
				.forEach(jobExecutionService::executeJob);
	}
	
	private void registerConfigurationJob(final JobProperties jobProperties) {
		logger.info("Registering job");
		
		final UUID uuid = timingService.start();
		final JobDefinition jobDefinition = jobDefinitionMapper.map(jobProperties);

		if (jobDefinitionService.existsByName(jobDefinition.getName())) {
			jobDefinitionService.deleteByName(jobDefinition.getName());
		}
		jobDefinitionService.save(jobDefinition);
		
		final Job job = jobService.createJobByJobDefinitionId(jobDefinition.getId());
		logger.info("Saving job took: " + timingService.stop(uuid) + "ms");
		
		jobRegistry.put(job.getId(), job);
	}
	
	public Job get(final Long jobId) {
		return jobRegistry.get(jobId);
	}
}
