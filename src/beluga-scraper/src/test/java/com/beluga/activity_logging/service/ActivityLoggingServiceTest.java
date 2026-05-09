package com.beluga.activity_logging.service;

import com.beluga.activity_logging.event.ActivityEvent;
import com.beluga.activity_logging.model.ActivityLog;
import com.beluga.activity_logging.repository.ActivityLogRepository;
import com.beluga.logging.ContextLogger;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ActivityLoggingServiceTest {
	
    @Mock
	private ContextLogger logger;
	@Mock
	private ActivityLogMapper activityLogMapper;
	@Mock
	private ActivityLogRepository activityLogRepository;
	
	@InjectMocks
	private ActivityLoggingService activityLoggingService;

	@Test
	void shouldOnActivityEvent() {
		final ActivityEvent activityEvent = mock(RETURNS_DEEP_STUBS);
		final ActivityLog activityLog = mock();
		when(activityLogMapper.map(activityEvent.getActivity())).thenReturn(activityLog);
		
		activityLoggingService.onActivityEvent(activityEvent);
		
		verify(activityLogRepository).save(activityLog);
	}
}