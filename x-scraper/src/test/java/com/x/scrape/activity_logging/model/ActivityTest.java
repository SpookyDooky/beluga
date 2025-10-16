package com.x.scrape.activity_logging.model;

import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.slf4j.MDC;

import java.time.Instant;
import java.util.Map;

import static com.x.scrape.activity_logging.model.ActivityType.REQUEST;
import static org.junit.jupiter.api.Assertions.assertEquals;
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
	
	@Test
	void shouldCopyMdc() {
		MDC.put("test", "test");
		
		final Activity activity = new ActivityImpl();
		
		final Map<String, String> context = activity.getContext();
		assertEquals("test", context.get("test"));
	}
	
	static class ActivityImpl extends Activity {
		
		public ActivityImpl() {
			super(REQUEST);
		}
	}
}