package com.x.scrape.service.task;

import com.x.scrape.mapper.ResultFileMapper;
import com.x.scrape.model.result.ResultFile;
import com.x.scrape.model.task.TaskExecution;
import com.x.scrape.model.task.event.TaskCompletedEvent;
import com.x.scrape.model.task.event.TaskFailedEvent;
import com.x.scrape.model.task.event.TaskStartedEvent;
import com.x.scrape.model.task.event.task_result.TaskResultEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;

import static com.x.scrape.model.task.TaskStatus.*;

// Todo move to event package -> which contains a package for all the event handlers
@Component
public class TaskExecutionEventHandler {
	
	private final TaskExecutionService taskExecutionService;
	private final ResultFileMapper resultFileMapper;
	
	public TaskExecutionEventHandler(final TaskExecutionService taskExecutionService,
	                                 final ResultFileMapper resultFileMapper) {
		this.taskExecutionService = taskExecutionService;
		this.resultFileMapper = resultFileMapper;
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
		try {
			final TaskExecution taskExecution = taskExecutionService.getById(event.getTaskId());
			taskExecution.setStatus(FAILED);
			
			taskExecutionService.save(taskExecution);
		} catch (final NullPointerException e) {
			throw e;
		}
	}
	
	@EventListener
	public void onTaskResult(final TaskResultEvent taskResultEvent) {
		final ResultFile resultFile = resultFileMapper.map(taskResultEvent);
		taskExecutionService.addResultFile(taskResultEvent.getTaskId(), resultFile);
	}
}
