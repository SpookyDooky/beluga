package com.beluga.execution.service.worker;

import com.beluga.execution.model.task.Task;
import com.beluga.execution.service.task.JobTaskQueue;
import com.beluga.logging.ContextLogger;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationEventPublisher;

import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.mockito.Mockito.reset;

@ExtendWith(MockitoExtension.class)
class WorkerOrchestratorTest {

	private static final Long JOB_ID = 123L;

	@Mock
	private ContextLogger logger;
	@Mock
	private ApplicationContext applicationContext;
	@Mock
	private ApplicationEventPublisher applicationEventPublisher;
	@Mock
	private JobTaskQueue jobTaskQueue;

	@InjectMocks
	private WorkerOrchestrator workerOrchestrator;

	@Mock
	private Worker worker;

	@Captor
	private ArgumentCaptor<WorkerTaskCompletedCallback> callbackArgumentCaptor;

	@BeforeEach
	void setup() {
		when(applicationContext.getBean(Worker.class)).thenReturn(worker);
	}

	@Test
	void shouldStartWorkers() {
		when(jobTaskQueue.isQueueEmpty(JOB_ID)).thenReturn(false);

		final Task task = mock();
		when(jobTaskQueue.pollTask(JOB_ID)).thenReturn(
				Optional.of(task),
				Optional.of(task)
		);

		workerOrchestrator.startWorkers(JOB_ID, 100, 1);

		verify(worker).init(eq(JOB_ID), callbackArgumentCaptor.capture());

		verify(worker, after(250)).execute(task);
		reset(worker);

		final WorkerTaskCompletedCallback callback = callbackArgumentCaptor.getValue();
		callback.complete(worker);

		verify(worker, after(250)).execute(task);
	}
}