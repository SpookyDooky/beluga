package com.x.scrape.service;

import com.x.scrape.execution.model.Job;
import com.x.scrape.mapper.job.JobMapper;
import com.x.scrape.mapper.task.TaskMapper;
import com.x.scrape.model.job_definition.JobDefinition;
import com.x.scrape.model.job_definition.JobExecution;
import com.x.scrape.model.task.Task;
import com.x.scrape.model.task.TaskDefinition;
import com.x.scrape.model.task.TaskExecution;
import jakarta.persistence.EntityManager;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class JobService {
	
	private final JobDefinitionService jobDefinitionService;
	private final JobMapper jobMapper;
	private final TaskMapper taskMapper;
	private final EntityManager entityManager;
	
	public JobService(final JobDefinitionService jobDefinitionService,
	                  final JobMapper jobMapper,
	                  final TaskMapper taskMapper,
	                  final Optional<EntityManager> entityManager) {
		this.jobDefinitionService = jobDefinitionService;
		this.jobMapper = jobMapper;
		this.taskMapper = taskMapper;
		this.entityManager = entityManager.orElse(null);
	}
	
	/**
	 * Creates a job that needs to be executed.
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
		
		return job;
	}
	
	private List<Task> createTasks(final JobDefinition jobDefinition,
	                               final Job job) {
		return jobDefinition.getTaskDefinitions()
				.stream()
				.map(taskDefinition -> createTask(jobDefinition, taskDefinition, job))
				.collect(Collectors.toList());
	}
	
	/**
	 * Creates a task and links it to a task execution to set the correct id for the task/
	 * @param jobDefinition the job definition this task belongs to.
	 * @param taskDefinition the task definition of this  task.
	 * @param job the job this task belongs to.
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
		
		if (entityManager != null) {
			entityManager.persist(taskExecution);
		} else {
			jobDefinitionService.save(jobDefinition);
		}
		
		final Task task = taskMapper.map(jobDefinition, taskDefinition);
		task.setId(taskExecution.getId());
		task.setJob(job);
		
		return task;
	}
}
