package com.beluga.execution.service.job;

import com.beluga.execution.event.job.JobFinishedEvent;
import com.beluga.execution.event.job.JobStartedEvent;
import com.beluga.service.job.JobDefinitionService;
import org.instancio.Instancio;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static com.beluga.model.job_definition.JobStatus.ACTIVE;
import static com.beluga.model.job_definition.JobStatus.COMPLETED;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class JobEventHandlerTest {
	
	@Mock
	private JobDefinitionService jobDefinitionService;
	
	@InjectMocks
	private JobEventHandler jobEventHandler;
	
	@Test
	void shouldOnJobStartedEvent() {
		final JobStartedEvent event = Instancio.create(JobStartedEvent.class);
		
		jobEventHandler.onJobStartedEvent(event);
		
		verify(jobDefinitionService).setJobExecutionStatusById(ACTIVE, event.getJobDefinitionId(), event.getJobExecutionId());
	}
	
	@Test
	void shouldOnJobFinishedEvent() {
		final JobFinishedEvent event = Instancio.create(JobFinishedEvent.class);
		
		jobEventHandler.onJobFinishedEvent(event);
		
		verify(jobDefinitionService).setJobExecutionStatusById(COMPLETED, event.getJobDefinitionId(), event.getJobExecutionId());
	}
}