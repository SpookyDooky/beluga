package com.x.scrape.api.task.controller;

import com.x.scrape.api.task.dto.PatchTaskDto;
import com.x.scrape.api.task.dto.ReadTaskDefinitionDto;
import com.x.scrape.api.task.dto.UpdateTaskDto;
import com.x.scrape.api.task.mapper.ReadTaskDefinitionDtoMapper;
import com.x.scrape.logging.CloseableContext;
import com.x.scrape.logging.ContextLogger;
import com.x.scrape.mapper.task.TaskDefinitionMapperService;
import com.x.scrape.model.job_definition.JobDefinition;
import com.x.scrape.model.job_definition.exception.TaskDefinitionNotFoundException;
import com.x.scrape.model.task.TaskDefinition;
import com.x.scrape.service.JobDefinitionService;
import com.x.scrape.service.exception.JobDefinitionNotFoundException;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.x.scrape.logging.ContextKeys.JOB_ID;
import static com.x.scrape.logging.ContextKeys.TASK_ID;

@RestController
@RequestMapping("/jobs/{jobId}/tasks")
public class TaskController {
	
	private final ContextLogger logger;
	private final TaskDefinitionMapperService taskDefinitionMapperService;
	private final JobDefinitionService jobDefinitionService;
	private final ReadTaskDefinitionDtoMapper taskDefinitionDtoMapper;
	
	public TaskController(final ContextLogger logger,
	                      final TaskDefinitionMapperService taskDefinitionMapperService,
	                      final JobDefinitionService jobDefinitionService,
	                      final ReadTaskDefinitionDtoMapper taskDefinitionDtoMapper) {
		this.logger = logger;
		this.taskDefinitionMapperService = taskDefinitionMapperService;
		this.jobDefinitionService = jobDefinitionService;
		this.taskDefinitionDtoMapper = taskDefinitionDtoMapper;
	}
	
	@GetMapping
	@Transactional
	public List<ReadTaskDefinitionDto> getTasks(@PathVariable("jobId") final Long jobId) {
		try (final CloseableContext ignored = logger.with(JOB_ID, jobId.toString())) {
			// Todo - set all existing task definitions to inactive
			final JobDefinition jobDefinition = jobDefinitionService.getById(jobId);
			
			return jobDefinition.getActiveTaskDefinitions()
					.stream()
					.map(taskDefinitionDtoMapper::map)
					.toList();
		}
	}
	
	@ExceptionHandler({
			JobDefinitionNotFoundException.class,
			TaskDefinitionNotFoundException.class
	})
	public ResponseEntity<Void> handleJobDefinitionNotFound() {
		return ResponseEntity.notFound()
				.build();
	}
	
	@GetMapping("/{taskId}")
	@Transactional
	public ReadTaskDefinitionDto getTask(@PathVariable("jobId") final Long jobId,
	                                     @PathVariable("taskId") final Long taskId) {
		try (final CloseableContext context = logger.with(JOB_ID, jobId.toString())) {
			context.put(TASK_ID, taskId.toString());
			
			final TaskDefinition taskDefinition = jobDefinitionService.getById(jobId)
					.getTaskDefinitionById(taskId);
			
			return taskDefinitionDtoMapper.map(taskDefinition);
		}
	}
	
	@PutMapping
	@Transactional
	public List<ReadTaskDefinitionDto> updateTasks(@PathVariable("jobId") final Long jobId,
	                                               @RequestBody final UpdateTaskDto tasks) {
		try (final CloseableContext ignored = logger.with(JOB_ID, jobId.toString())) {
			final List<TaskDefinition> taskDefinitions = taskDefinitionMapperService.map(tasks.getUrls());
			
			final JobDefinition jobDefinition = jobDefinitionService.getById(jobId);
			jobDefinition.setTaskDefinitions(taskDefinitions);
			
			jobDefinitionService.save(jobDefinition);

			return jobDefinition.getTaskDefinitions()
					.stream()
					.map(taskDefinitionDtoMapper::map)
					.toList();
		}
	}
	
	@PatchMapping
	@Transactional
	public List<ReadTaskDefinitionDto> updateTasks(@PathVariable("jobId") final Long jobId,
	                                               @RequestBody final PatchTaskDto patchTaskDto) {
		try (final CloseableContext ignored = logger.with(JOB_ID, jobId.toString())) {
			jobDefinitionService.setTaskDefinitionsInactiveByUrl(jobId, patchTaskDto.getRemove());
			
			final List<TaskDefinition> taskDefinitions = taskDefinitionMapperService.map(patchTaskDto.getAdd());
			
			final JobDefinition jobDefinition = jobDefinitionService.getById(jobId);
			jobDefinition.addTaskDefinitions(taskDefinitions);
			
			jobDefinitionService.save(jobDefinition);
			
			return jobDefinition.getActiveTaskDefinitions()
					.stream()
					.map(taskDefinitionDtoMapper::map)
					.toList();
		}
	}
}
