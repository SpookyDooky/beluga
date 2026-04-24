package com.x.scrape.api.task.controller;

import com.x.scrape.api.task.dto.PatchTaskDto;
import com.x.scrape.api.task.dto.ReadTaskDefinitionDto;
import com.x.scrape.api.task.dto.UpdateTaskDto;
import com.x.scrape.api.task.mapper.ReadTaskDefinitionDtoMapper;
import com.x.scrape.execution.model.task.TaskDefinition;
import com.x.scrape.logging.CloseableContext;
import com.x.scrape.logging.ContextLogger;
import com.x.scrape.mapper.task.TaskDefinitionMapperService;
import com.x.scrape.model.job_definition.JobDefinition;
import com.x.scrape.model.job_definition.exception.TaskDefinitionNotFoundException;
import com.x.scrape.service.exception.JobDefinitionNotFoundException;
import com.x.scrape.service.job.JobDefinitionService;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.x.scrape.logging.ContextKeys.JOB_ID;
import static com.x.scrape.logging.ContextKeys.TASK_ID;

@RestController
@RequestMapping("/jobs/{jobDefinitionId}/tasks")
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
	public List<ReadTaskDefinitionDto> getTasks(@PathVariable("jobDefinitionId") final Long jobDefinitionId) {
		try (final CloseableContext ignored = logger.with(JOB_ID, jobDefinitionId.toString())) {
			final JobDefinition jobDefinition = jobDefinitionService.getById(jobDefinitionId);
			
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
	public ReadTaskDefinitionDto getTask(@PathVariable("jobDefinitionId") final Long jobDefinitionId,
	                                     @PathVariable("taskId") final Long taskId) {
		try (final CloseableContext context = logger.with(JOB_ID, jobDefinitionId.toString())) {
			context.put(TASK_ID, taskId.toString());
			
			final TaskDefinition taskDefinition = jobDefinitionService.getById(jobDefinitionId)
					.getTaskDefinitionById(taskId);
			
			return taskDefinitionDtoMapper.map(taskDefinition);
		}
	}
	
	@PutMapping
	@Transactional
	public List<ReadTaskDefinitionDto> updateTasks(@PathVariable("jobDefinitionId") final Long jobDefinitionId,
	                                               @RequestBody final UpdateTaskDto tasks) {
		try (final CloseableContext ignored = logger.with(JOB_ID, jobDefinitionId.toString())) {
			final List<TaskDefinition> taskDefinitions = taskDefinitionMapperService.map(tasks.getUrls());
			
			final JobDefinition jobDefinition = jobDefinitionService.getById(jobDefinitionId);
			// TODO - Check what happens with duplicate tasks? If it is added but the current one is inactive. (It should be set to active)
			// TODO - If a task does not have a result yet it can be removed, otherwise it should be set to inactive
			jobDefinition.setExistingTaskDefinitionsToInactive();
			jobDefinition.setTaskDefinitions(taskDefinitions);
			
			jobDefinitionService.save(jobDefinition);

			return jobDefinition.getActiveTaskDefinitions()
					.stream()
					.map(taskDefinitionDtoMapper::map)
					.toList();
		}
	}
	
	@PatchMapping
	@Transactional
	public List<ReadTaskDefinitionDto> updateTasks(@PathVariable("jobDefinitionId") final Long jobDefinitionId,
	                                               @RequestBody final PatchTaskDto patchTaskDto) {
		try (final CloseableContext ignored = logger.with(JOB_ID, jobDefinitionId.toString())) {
			jobDefinitionService.setTaskDefinitionsInactiveByUrl(jobDefinitionId, patchTaskDto.getRemove());
			
			final List<TaskDefinition> taskDefinitions = taskDefinitionMapperService.map(patchTaskDto.getAdd());
			jobDefinitionService.addTaskDefinitionsById(
					taskDefinitions,
					jobDefinitionId
			);
			
			return jobDefinitionService.getActiveTaskDefinitionsById(jobDefinitionId)
					.stream()
					.map(taskDefinitionDtoMapper::map)
					.toList();
		}
	}
}
