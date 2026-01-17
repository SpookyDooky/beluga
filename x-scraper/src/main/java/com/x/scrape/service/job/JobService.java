package com.x.scrape.service.job;

import com.x.scrape.execution.model.Job;
import com.x.scrape.mapper.job.JobMapper;
import com.x.scrape.mapper.task.TaskMapper;
import com.x.scrape.model.job_definition.JobDefinition;
import com.x.scrape.model.job_definition.JobExecution;
import com.x.scrape.model.job_definition.JobStatus;
import com.x.scrape.model.task.Task;
import com.x.scrape.model.task.TaskDefinition;
import com.x.scrape.model.task.TaskExecution;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.x.scrape.model.task.TaskStatus.PAUSED;

@Service
public class JobService {
	
	private final JobDefinitionService jobDefinitionService;
	private final JobMapper jobMapper;
	private final TaskMapper taskMapper;

	public JobService(final JobDefinitionService jobDefinitionService,
	                  final JobMapper jobMapper,
	                  final TaskMapper taskMapper) {
		this.jobDefinitionService = jobDefinitionService;
		this.jobMapper = jobMapper;
		this.taskMapper = taskMapper;
	}
	
	/**
	 * Creates a job that needs to be executed.
	 *
	 * @param jobDefinitionId {@link JobDefinition} id.
	 * @return a new {@link Job}
	 */
	@Transactional
	public Job createJobByJobDefinitionId(final Long jobDefinitionId) {
		final JobDefinition jobDefinition = jobDefinitionService.getById(jobDefinitionId);
		
		jobDefinition.addExecution(new JobExecution());
		jobDefinitionService.save(jobDefinition);
		
		final Job job = jobMapper.map(jobDefinition);
		
		job.setId(jobDefinition.getMostRecentExecution().get().getId());
		job.setTasks(createTasks(jobDefinition, job));
		
		jobDefinitionService.save(jobDefinition);
		
		return job;
	}
	
	private List<Task> createTasks(final JobDefinition jobDefinition,
	                               final Job job) {
		return jobDefinition.getActiveTaskDefinitions()
				.stream()
				.map(taskDefinition -> createTask(jobDefinition, taskDefinition, job))
				.collect(Collectors.toList());
	}
	
	/**
	 * Creates a task and links it to a task execution to set the correct id for the task/
	 *
	 * @param jobDefinition  the job definition this task belongs to.
	 * @param taskDefinition the task definition of this  task.
	 * @param job            the job this task belongs to.
	 * @return task for the {@link TaskDefinition}.
	 */
	private Task createTask(final JobDefinition jobDefinition,
	                        final TaskDefinition taskDefinition,
	                        final Job job) {
		final JobExecution jobExecution = jobDefinition.getMostRecentExecution()
				.get();
		
		final TaskExecution taskExecution = new TaskExecution();
		taskExecution.setTaskDefinition(taskDefinition);
		
		jobExecution.addTask(taskExecution);
		
		final Task task = taskMapper.map(jobDefinition, taskDefinition);
		task.setId(taskExecution.getId());
		task.setJob(job);
		
		return task;
	}
	
	/**
	 * Creates a {@link Job} for a {@link Job} that has previously been paused.
	 *
	 * @param jobDefinitionId the id of the {@link JobDefinition}.
	 * @return optional {@link Job} if there is no previous execution or the previous execution has been completed it will be empty.
	 */
	@Transactional
	public Optional<Job> createResumedJob(final Long jobDefinitionId) {
		final JobDefinition jobDefinition = jobDefinitionService.getById(jobDefinitionId);
		final Optional<JobExecution> latestExecutionOptional = jobDefinition.getMostRecentExecution();
		
		if (latestExecutionOptional.isEmpty()) {
			return Optional.empty();
		}
		
		return createResumedJob(latestExecutionOptional.get());
	}
	
	private Optional<Job> createResumedJob(final JobExecution jobExecution) {
		if (jobExecution.getStatus() != JobStatus.PAUSED) {
			return Optional.empty();
		}
		
		final List<TaskExecution> pausedTasks = jobExecution.getTasksByStatus(PAUSED);
		if (pausedTasks.isEmpty()) {
			return Optional.empty();
		}
		
		final JobDefinition jobDefinition = jobExecution.getJobDefinition();
		
		return Optional.of(createResumedJob(pausedTasks, jobDefinition, jobExecution));
	}
	
	private Job createResumedJob(final List<TaskExecution> taskExecutions,
	                             final JobDefinition jobDefinition,
	                             final JobExecution jobExecution) {
		final Job job = jobMapper.map(jobDefinition);
		job.setId(jobExecution.getId());
		
		job.setTasks(
				taskExecutions.stream()
						.map(taskExecution -> createTask(taskExecution, jobDefinition, job))
						.toList()
		);
		
		return job;
	}
	
	private Task createTask(final TaskExecution taskExecution,
	                        final JobDefinition jobDefinition,
	                        final Job job) {
		final TaskDefinition taskDefinition = taskExecution.getTaskDefinition();
		
		final Task task = taskMapper.map(jobDefinition, taskDefinition);
		task.setId(taskExecution.getId());
		task.setJob(job);
		
		return task;
	}
}
