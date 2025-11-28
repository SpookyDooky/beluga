package com.x.scrape.api.task.controller;

import com.x.scrape.api.task.dto.UpdateTaskDto;
import com.x.scrape.logging.ContextLogger;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/jobs/{jobId}/tasks")
public class TaskController {
	
	private final ContextLogger logger;
	
	public TaskController(final ContextLogger logger) {
		this.logger = logger;
	}
	
	@PutMapping
	@Transactional
	public void updateTasks(@PathVariable final Long jobId,
	                        @RequestBody final UpdateTaskDto tasks) {
		
	}
}
