package com.x.scrape.service.task;

import com.x.scrape.model.task.TaskExecution;
import com.x.scrape.model.task.event.TaskCompletedEvent;
import com.x.scrape.model.task.event.TaskFailedEvent;
import com.x.scrape.model.task.event.TaskStartedEvent;
import org.instancio.Instancio;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static com.x.scrape.model.task.TaskStatus.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TaskExecutionEventHandlerTest {
	
	@Mock
	private TaskExecutionService taskExecutionService;
	
	@InjectMocks
	private TaskExecutionEventHandler taskExecutionEventHandler;
	
	@Test
	void shouldOnTaskStarted() {
		final TaskStartedEvent event = Instancio.create(TaskStartedEvent.class);
		final TaskExecution taskExecution = new TaskExecution();
		when(taskExecutionService.getById(event.getTaskId())).thenReturn(taskExecution);
		
		taskExecutionEventHandler.onTaskStarted(event);
		
		assertEquals(ACTIVE, taskExecution.getStatus());
		assertNotNull(taskExecution.getExecutedAt());
		
		verify(taskExecutionService).save(taskExecution);
	}
	
	@Test
	void onTaskCompleted() {
		final TaskCompletedEvent event = Instancio.create(TaskCompletedEvent.class);
		final TaskExecution taskExecution = new TaskExecution();
		when(taskExecutionService.getById(event.getTaskId())).thenReturn(taskExecution);
		
		taskExecutionEventHandler.onTaskCompleted(event);
		
		assertEquals(COMPLETED, taskExecution.getStatus());
		assertEquals(event.getResultFolder(), taskExecution.getResultFolder());
		
		verify(taskExecutionService).save(taskExecution);
	}
	
	@Test
	void onTaskFailed() {
		final TaskFailedEvent event = Instancio.create(TaskFailedEvent.class);
		final TaskExecution taskExecution = new TaskExecution();
		when(taskExecutionService.getById(event.getTaskId())).thenReturn(taskExecution);
		
		taskExecutionEventHandler.onTaskFailed(event);
		
		assertEquals(FAILED, taskExecution.getStatus());
		
		verify(taskExecutionService).save(taskExecution);
	}
}