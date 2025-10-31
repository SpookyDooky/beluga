package com.x.scrape.service.task;

import com.x.scrape.model.task.TaskExecution;
import com.x.scrape.model.task.event.TaskCompletedEvent;
import com.x.scrape.model.task.event.TaskFailedEvent;
import com.x.scrape.model.task.event.TaskStartedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;

import static com.x.scrape.model.task.TaskStatus.*;

@Component
public class TaskExecutionEventHandler {
	
	private final TaskExecutionService taskExecutionService;
	
	public TaskExecutionEventHandler(final TaskExecutionService taskExecutionService) {
		this.taskExecutionService = taskExecutionService;
	}
	
	@EventListener
	@Transactional
	public void onTaskStarted(final TaskStartedEvent event) {
		final TaskExecution taskExecution = taskExecutionService.getById(event.getTaskId());
		taskExecution.setStatus(ACTIVE);
		taskExecution.setExecutedAt(Instant.now());
		
		taskExecutionService.save(taskExecution);
	}
	
	@EventListener
	@Transactional
	public void onTaskCompleted(final TaskCompletedEvent event) {
		final TaskExecution taskExecution = taskExecutionService.getById(event.getTaskId());
		taskExecution.setStatus(COMPLETED);
		taskExecution.setResultFolder(event.getResultFolder());
		
		taskExecutionService.save(taskExecution);
	}
	
	@EventListener
	@Transactional
	public void onTaskFailed(final TaskFailedEvent event) {
		final TaskExecution taskExecution = taskExecutionService.getById(event.getTaskId());
		taskExecution.setStatus(FAILED);
		
		taskExecutionService.save(taskExecution);
	}
}
