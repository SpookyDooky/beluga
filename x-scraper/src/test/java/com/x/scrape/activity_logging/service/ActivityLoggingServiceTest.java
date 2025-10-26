package com.x.scrape.activity_logging.service;

import com.x.scrape.activity_logging.event.ActivityEvent;
import com.x.scrape.activity_logging.event.ActivityFlushEvent;
import com.x.scrape.activity_logging.model.Activity;
import com.x.scrape.activity_logging.model.RequestActivity;
import com.x.scrape.execution.model.Job;
import com.x.scrape.execution.service.job.JobRegistry;
import com.x.scrape.logging.ContextLogger;
import org.instancio.Instancio;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationEventPublisher;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

import static com.x.scrape.logging.ContextKeys.JOB_EXECUTION_ID;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ActivityLoggingServiceTest {

    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd_hh-mm-ss");

    @Mock
	private ContextLogger logger;
	@Mock
	private ApplicationEventPublisher applicationEventPublisher;
	@Mock
	private JobRegistry jobRegistry;
	
	@InjectMocks
	private ActivityLoggingService activityLoggingService;

    @Captor
    private ArgumentCaptor<ActivityFlushEvent> eventArgumentCaptor;

	@Test
	void shouldHandleActivityEvent() {
		final Long jobId = 123L;
		final Activity activity = createActivity(jobId);
		
		final ActivityEvent event = new ActivityEvent(activity);
		
		// Supply event
		activityLoggingService.onActivityEvent(event);
		
		verifyNoInteractions(applicationEventPublisher, jobRegistry);
		
		final Job job = Instancio.create(Job.class);
		when(jobRegistry.get(jobId)).thenReturn(job);

        final Instant currentTime = Instant.now();
        try (final MockedStatic<Instant> mockedInstant = mockStatic(Instant.class)) {
            mockedInstant.when(Instant::now).thenReturn(currentTime);

            // Flush all activities
            activityLoggingService.flushActivityLogs();

            verify(applicationEventPublisher).publishEvent(eventArgumentCaptor.capture());
            final ActivityFlushEvent actualEvent = eventArgumentCaptor.getValue();

            assertEquals(job.getJobFolder() + "/logs", actualEvent.getStorageHint().getFolder());
            assertEquals(
                    DATE_TIME_FORMATTER.format(LocalDateTime.ofInstant(currentTime, ZoneId.systemDefault())) + ".json",
                    actualEvent.getStorageHint().getFileName()
            );
        }
		
	}
	
	RequestActivity createActivity(final Long jobId) {
		final RequestActivity activity =  new RequestActivity(mock(), 1L, false);
		
		activity.getContext()
				.put(JOB_EXECUTION_ID, jobId.toString());
		
		return activity;
	}
}