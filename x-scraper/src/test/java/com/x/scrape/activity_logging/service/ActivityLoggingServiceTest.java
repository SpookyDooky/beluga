package com.x.scrape.activity_logging.service;

import com.x.scrape.activity_logging.event.ActivityEvent;
import com.x.scrape.activity_logging.event.ActivityFlushEvent;
import com.x.scrape.activity_logging.model.Activity;
import com.x.scrape.activity_logging.model.RequestActivity;
import com.x.scrape.logging.ContextLogger;
import com.x.scrape.model.job.JobDefinition;
import com.x.scrape.scraping.job.JobRegistry;
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
import java.util.UUID;

import static com.x.scrape.logging.ContextKeys.JOB_UUID;
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
		final UUID jobId = UUID.randomUUID();
		final Activity activity = createActivity(jobId);
		
		final ActivityEvent event = new ActivityEvent(activity);
		
		// Supply event
		activityLoggingService.onActivityEvent(event);
		
		verifyNoInteractions(applicationEventPublisher, jobRegistry);
		
		final JobDefinition jobDefinition = Instancio.create(JobDefinition.class);
		when(jobRegistry.get(jobId)).thenReturn(jobDefinition);

        final Instant currentTime = Instant.now();
        try (final MockedStatic<Instant> mockedInstant = mockStatic(Instant.class)) {
            mockedInstant.when(Instant::now).thenReturn(currentTime);

            // Flush all activities
            activityLoggingService.flushActivityLogs();

            verify(applicationEventPublisher).publishEvent(eventArgumentCaptor.capture());
            final ActivityFlushEvent actualEvent = eventArgumentCaptor.getValue();

            assertEquals(jobDefinition.getJobFolder() + "/logs", actualEvent.getStorageHint().getFolder());
            assertEquals(
                    DATE_TIME_FORMATTER.format(LocalDateTime.ofInstant(currentTime, ZoneId.systemDefault())) + ".json",
                    actualEvent.getStorageHint().getFileName()
            );
        }
		
	}
	
	RequestActivity createActivity(final UUID jobId) {
		final RequestActivity activity =  new RequestActivity(mock(), 1L, false);
		
		activity.getContext()
				.put(JOB_UUID, jobId.toString());
		
		return activity;
	}
}