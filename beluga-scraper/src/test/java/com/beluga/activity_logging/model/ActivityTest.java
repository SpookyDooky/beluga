package com.beluga.activity_logging.model;

import com.beluga.activity_logging.activitiy.Activity;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;

import java.time.Instant;

import static com.beluga.activity_logging.activitiy.ActivityType.REQUEST;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.Mockito.mockStatic;

class ActivityTest {
	
	@Test
	void shouldSetTimestamp() {
		final Instant expectedTimestamp = Instant.now();
		try (final MockedStatic<Instant> instant = mockStatic(Instant.class)) {
			instant.when(Instant::now).thenReturn(expectedTimestamp);
			
			final Activity activity = new ActivityImpl();
			
			assertSame(expectedTimestamp, activity.getTimestamp());
		}
	}
	
	static class ActivityImpl extends Activity {
		
		public ActivityImpl() {
			super(REQUEST);
		}
	}
}