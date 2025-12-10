package com.x.scrape.service.job;

import com.x.scrape.execution.event.job.JobStartedEvent;
import org.instancio.Instancio;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static com.x.scrape.model.job_definition.JobStatus.ACTIVE;
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
}