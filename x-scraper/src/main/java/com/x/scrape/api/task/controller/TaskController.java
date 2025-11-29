package com.x.scrape.api.task.controller;

import com.x.scrape.api.task.dto.UpdateTaskDto;
import com.x.scrape.api.task.dto.ReadTaskDefinitionDto;
import com.x.scrape.api.task.mapper.ReadTaskDefinitionDtoMapper;
import com.x.scrape.logging.CloseableContext;
import com.x.scrape.logging.ContextLogger;
import com.x.scrape.mapper.task.TaskDefinitionMapperService;
import com.x.scrape.model.job_definition.JobDefinition;
import com.x.scrape.model.task.TaskDefinition;
import com.x.scrape.service.JobDefinitionService;
import com.x.scrape.service.exception.JobDefinitionNotFoundException;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.x.scrape.logging.ContextKeys.JOB_ID;

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
	
	@PutMapping
	@Transactional
	public List<ReadTaskDefinitionDto> updateTasks(@PathVariable final Long jobId,
	                                               @RequestBody final UpdateTaskDto tasks) {
		try (final CloseableContext ignored = logger.with(JOB_ID, jobId.toString())) {
			final List<TaskDefinition> taskDefinitions = taskDefinitionMapperService.map(tasks.getUrls());
			
			final JobDefinition jobDefinition = jobDefinitionService.getById(jobId);
			jobDefinition.getTaskDefinitions().clear();
			jobDefinition.getTaskDefinitions().addAll(taskDefinitions);
			
			jobDefinitionService.save(jobDefinition);

			return jobDefinition.getTaskDefinitions()
					.stream()
					.map(taskDefinitionDtoMapper::map)
					.toList();
		}
	}
	
	@ExceptionHandler(JobDefinitionNotFoundException.class)
	public ResponseEntity<Void> handleJobDefinitionNotFound() {
		return ResponseEntity.notFound()
				.build();
	}
}
