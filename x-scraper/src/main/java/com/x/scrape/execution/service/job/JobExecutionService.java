package com.x.scrape.execution.service.job;

import com.x.scrape.execution.event.job.JobFinishedEvent;
import com.x.scrape.execution.event.job.JobStartedEvent;
import com.x.scrape.execution.model.Job;
import com.x.scrape.execution.service.task.JobTaskQueue;
import com.x.scrape.execution.service.worker.Worker;
import com.x.scrape.execution.service.worker.WorkerOrchestrator;
import com.x.scrape.execution.service.worker.event.JobWorkersFinishedEvent;
import com.x.scrape.logging.ContextLogger;
import com.x.scrape.model.job_definition.JobDefinition;
import com.x.scrape.model.job_definition.configuration.execution_configuration.ExecutionConfiguration;
import com.x.scrape.model.task.Task;
import com.x.scrape.service.task.TaskExecutionService;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

import static com.x.scrape.model.task.TaskStatus.STOPPED;

/**
 * This service takes care of starting the correct amount of {@link Worker}'s for each {@link JobDefinition}.
 * Furthermore, for each {@link JobDefinition} it also places all tasks in the {@link JobTaskQueue}.
 */
@Service
public class JobExecutionService {
	
	private final ContextLogger logger;
	private final JobTaskQueue jobTaskQueue;
	private final ApplicationEventPublisher eventPublisher;
	private final WorkerOrchestrator workerOrchestrator;
	private final TaskExecutionService taskExecutionService;
	
	private final Map<Long, Job> jobIdJobMap = new HashMap<>();
	
	public JobExecutionService(final ContextLogger logger,
	                           final JobTaskQueue jobTaskQueue,
	                           final ApplicationEventPublisher eventPublisher,
	                           final WorkerOrchestrator workerOrchestrator,
	                           final TaskExecutionService taskExecutionService) {
		this.logger = logger;
		this.jobTaskQueue = jobTaskQueue;
		this.eventPublisher = eventPublisher;
		this.workerOrchestrator = workerOrchestrator;
		this.taskExecutionService = taskExecutionService;
	}
	
	public void executeJob(final Job job) {
		logger.info("Executing job");
		
		
		job.getTasks().forEach(jobTaskQueue::offerTask);
		job.getTasks().clear();
		
		job.createJobFolders();
		
		final ExecutionConfiguration executionConfiguration = job.getExecutionConfiguration();
		
		workerOrchestrator.startWorkers(
				job.getId(),
				executionConfiguration.getTasksPerSecond(),
				executionConfiguration.getWorkers()
		);
		
		jobIdJobMap.put(job.getId(), job);
		
		eventPublisher.publishEvent(new JobStartedEvent(job.getJobDefinitionId(), job.getId()));
	}
	
	// Todo we should keep track of failed workers, because that means tasks failed these should be reran
	// To actually complete the job
	@EventListener
	public void onJobWorkersFinishedEvent(final JobWorkersFinishedEvent event) {
		final Job job = jobIdJobMap.get(event.getJobId());
		
		eventPublisher.publishEvent(new JobFinishedEvent(job.getJobDefinitionId(), job.getId()));
	}
	
	/**
	 * Stops a running {@link Job}, and takes care of changing the status of all remaining tasks.
	 *
	 * @param jobId the id of the {@link Job}.
	 */
	public void stop(final Long jobId) {
		jobTaskQueue.clearTasks(jobId).stream()
				.map(Task::getId)
				.forEach(taskId -> taskExecutionService.setStatusById(taskId, STOPPED));
	}
}
