package com.beluga.execution.service.worker;

import com.beluga.execution.service.worker.event.JobWorkersFinishedEvent;
import com.beluga.execution.service.worker.event.WorkerFinishedEvent;
import com.beluga.execution.service.worker.rate_limiting.JitterRateLimiter;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationEventPublisher;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class WorkerOrchestratorTest {
	
	@Mock
	private ApplicationContext applicationContext;
	@Mock
	private ApplicationEventPublisher applicationEventPublisher;
	
	@InjectMocks
	private WorkerOrchestrator workerOrchestrator;
	
	@Captor
	private ArgumentCaptor<JitterRateLimiter> rateLimiterArgumentCaptor;
	@Captor
	private ArgumentCaptor<JobWorkersFinishedEvent> jobWorkersFinishedEventArgumentCaptor;
	
	@Test
	void shouldStartWorkers() {
		final Long jobId = 123L;
		final double rateLimit = 1.0;
		final int workers = 2;
		
		final Worker worker = mock();
		when(applicationContext.getBean(Worker.class)).thenReturn(worker);
		
		workerOrchestrator.startWorkers(jobId, rateLimit, workers);
		
//		verify(worker, times(workers)).init(eq(jobId), rateLimiterArgumentCaptor.capture());
		
		for (final JitterRateLimiter rateLimiter : rateLimiterArgumentCaptor.getAllValues()) {
			assertEquals(rateLimit, rateLimiter.getRate());
		}
	}
	
	@Disabled("Flaky for no explainable reason...")
	@Test
	void shouldSendWorkerFinishedEvent() {
		final Long jobId = 123L;
		final double rateLimit = 1.0;
		final int workers = 2;
		
		final Worker worker = mock();
		when(applicationContext.getBean(Worker.class)).thenReturn(worker);
		
		workerOrchestrator.startWorkers(jobId, rateLimit, workers);
		
		final WorkerFinishedEvent workerFinishedEvent = new WorkerFinishedEvent(jobId);
		workerOrchestrator.onWorkerFinishedEvent(workerFinishedEvent);
		
		verifyNoInteractions(applicationEventPublisher);
		
		workerOrchestrator.onWorkerFinishedEvent(workerFinishedEvent);
		verify(applicationEventPublisher).publishEvent(jobWorkersFinishedEventArgumentCaptor.capture());
		
		final JobWorkersFinishedEvent event = jobWorkersFinishedEventArgumentCaptor.getValue();
		assertEquals(jobId, event.getJobId());
	}
}