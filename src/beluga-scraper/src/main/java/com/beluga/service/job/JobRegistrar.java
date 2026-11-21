package com.beluga.service.job;

import com.beluga.logging.CloseableContext;
import com.beluga.logging.ContextLogger;
import com.beluga.mapper.job.JobDefinitionMapper;
import com.beluga.model.job_definition.JobDefinition;
import com.beluga.properties.BelugaScraperProperties;
import com.beluga.properties.scraping.JobProperties;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

/**
 * Registers the {@link JobDefinition}'s configured through {@link BelugaScraperProperties} when the application starts.
 * As an {@link ApplicationRunner} it finishes before the {@code ApplicationReadyEvent} is published, so the
 * definitions are in place before {@link com.beluga.execution.service.job.JobScheduler} recovers unfinished
 * executions, and a failure stops startup.
 */
@Component
public class JobRegistrar implements ApplicationRunner {

	private final ContextLogger logger;
	private final BelugaScraperProperties belugaScraperProperties;
	private final JobDefinitionMapper jobDefinitionMapper;
	private final JobDefinitionService jobDefinitionService;
	
	public JobRegistrar(final ContextLogger logger,
	                    final BelugaScraperProperties belugaScraperProperties,
	                    final JobDefinitionMapper jobDefinitionMapper,
	                    final JobDefinitionService jobDefinitionService) {
		this.logger = logger;
		this.belugaScraperProperties = belugaScraperProperties;
		this.jobDefinitionMapper = jobDefinitionMapper;
		this.jobDefinitionService = jobDefinitionService;
	}

	@Override
	public void run(final ApplicationArguments args) {
		registerJobs();
	}

	/**
	 * This method only registers {@link JobDefinition}'s that are configured through {@link BelugaScraperProperties}
	 */
	public void registerJobs() {
		if (belugaScraperProperties.getJobs() == null) {
			return;
		}
		
		belugaScraperProperties.getJobs()
				.forEach(this::registerConfigurationJob);
	}
	
	private void registerConfigurationJob(final JobProperties jobProperties) {
		try (final CloseableContext ignored = logger.with(jobProperties)) {
			logger.info("Registering job");

			final JobDefinition jobDefinition = jobDefinitionMapper.map(jobProperties);

			if (jobDefinitionService.existsByName(jobDefinition.getName())) {
				logger.info("Job already existed with name, overwriting existing job");
				jobDefinitionService.deleteByName(jobDefinition.getName());
			}

			jobDefinitionService.save(jobDefinition);
		}
	}
}
