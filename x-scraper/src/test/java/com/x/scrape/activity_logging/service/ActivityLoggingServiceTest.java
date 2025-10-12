package com.x.scrape.activity_logging.service;

import com.x.scrape.activity_logging.event.ActivityEvent;
import com.x.scrape.activity_logging.model.Activity;
import com.x.scrape.activity_logging.model.RequestActivity;
import com.x.scrape.logging.ContextLogger;
import com.x.scrape.model.job.Job;
import com.x.scrape.scraping.job.JobRegistry;
import org.instancio.Instancio;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationEventPublisher;

import java.util.UUID;

import static com.x.scrape.logging.ContextKeys.JOB_ID;
import static org.instancio.Select.field;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ActivityLoggingServiceTest {

	@Mock
	private ContextLogger logger;
	@Mock
	private ApplicationEventPublisher applicationEventPublisher;
	@Mock
	private JobRegistry jobRegistry;
	
	@InjectMocks
	private ActivityLoggingService activityLoggingService;
	
	@Test
	void shouldHandleActivityEvent() {
		final UUID jobId = UUID.randomUUID();
		final Activity activity = createActivity(jobId);
		
		final ActivityEvent event = new ActivityEvent(activity);
		
		// Supply event
		activityLoggingService.onActivityEvent(event);
		
		verifyNoInteractions(applicationEventPublisher, jobRegistry);
		
		final Job job = Instancio.create(Job.class);
		when(jobRegistry.get(jobId)).thenReturn(job);
		
		// Flush all activities
		activityLoggingService.flushActivityLogs();
		
	}
	
	RequestActivity createActivity(final UUID jobId) {
		final RequestActivity activity =  Instancio.of(RequestActivity.class)
				.set(field(RequestActivity::getUrl), mock())
				.create();
		
		activity.getContext()
				.put(JOB_ID, jobId.toString());
		
		return activity;
	}
}