package com.x.scrape.execution.service.job;

import com.google.common.util.concurrent.RateLimiter;
import com.x.scrape.execution.model.Job;
import com.x.scrape.execution.service.task.JobTaskQueue;
import com.x.scrape.execution.service.worker.Worker;
import com.x.scrape.logging.ContextLogger;
import com.x.scrape.model.job_definition.JobDefinition;
import com.x.scrape.model.job_definition.configuration.execution_configuration.ExecutionConfiguration;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

/**
 * This service takes care of starting the correct amount of {@link Worker}'s for each {@link JobDefinition}.
 * Furthermore, for each {@link JobDefinition} it also places all tasks in the {@link JobTaskQueue}.
 */
@Service
public class JobExecutionService {
	
	private final ContextLogger logger;
	private final ApplicationContext applicationContext;
	private final JobTaskQueue jobTaskQueue;
	
	public JobExecutionService(final ContextLogger logger,
	                           final ApplicationContext applicationContext,
	                           final JobTaskQueue jobTaskQueue) {
		this.logger = logger;
		this.applicationContext = applicationContext;
		this.jobTaskQueue = jobTaskQueue;
	}
	
	public void executeJob(final Job job) {
		logger.info("Executing job");
		
		job.getTasks()
				.forEach(jobTaskQueue::offerTask);
		
		job.createJobFolders();
		
		final ExecutionConfiguration executionConfiguration = job.getExecutionConfiguration();
		final RateLimiter rateLimiter = RateLimiter.create((double) executionConfiguration.getTasksPerSecond());
		
		for (int i = 0; i < executionConfiguration.getWorkers(); i++) {
			final Worker worker = applicationContext.getBean(Worker.class);
			worker.init(
					job.getId(),
					rateLimiter
			);
			
			new Thread(worker::start)
					.start();
		}
	}
}
