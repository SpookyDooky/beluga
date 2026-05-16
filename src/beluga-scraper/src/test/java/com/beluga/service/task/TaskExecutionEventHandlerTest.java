package com.beluga.service.task;

import com.beluga.execution.event.task.TaskCompletedEvent;
import com.beluga.execution.event.task.TaskFailedEvent;
import com.beluga.execution.event.task.TaskStartedEvent;
import com.beluga.execution.event.task.task_result.TaskResultStoredEvent;
import com.beluga.execution.model.task.TaskExecution;
import com.beluga.mapper.ResultFileMapper;
import com.beluga.model.result.ResultFile;
import org.instancio.Instancio;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static com.beluga.execution.model.task.TaskStatus.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TaskExecutionEventHandlerTest {
	
	@Mock
	private TaskExecutionService taskExecutionService;
	@Mock
	private ResultFileMapper resultFileMapper;
	
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
	
	@Test
	void onTaskResultStored() {
		final TaskResultStoredEvent taskResultStoredEvent = Instancio.create(TaskResultStoredEvent.class);
		final ResultFile resultFile = mock();
		when(resultFileMapper.map(taskResultStoredEvent)).thenReturn(resultFile);
		
		taskExecutionEventHandler.onTaskResultStored(taskResultStoredEvent);
		
		verify(taskExecutionService).addResultFile(taskResultStoredEvent.getTaskId(), resultFile);
	}
}