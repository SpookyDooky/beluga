package com.beluga.service.task;

import com.beluga.mapper.ResultFileMapper;
import com.beluga.model.result.ResultFile;
import com.beluga.execution.model.task.TaskExecution;
import com.beluga.execution.event.task.TaskCompletedEvent;
import com.beluga.execution.event.task.TaskFailedEvent;
import com.beluga.execution.event.task.TaskStartedEvent;
import com.beluga.execution.event.task.task_result.TaskResultEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;

import static com.beluga.execution.model.task.TaskStatus.*;

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
