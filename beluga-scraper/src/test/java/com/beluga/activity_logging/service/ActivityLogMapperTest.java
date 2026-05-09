package com.beluga.activity_logging.service;

import com.beluga.activity_logging.activitiy.Activity;
import com.beluga.activity_logging.model.ActivityLog;
import org.junit.jupiter.api.Test;
import tools.jackson.databind.json.JsonMapper;

import java.time.Instant;

import static com.beluga.activity_logging.activitiy.ActivityType.REQUEST;
import static java.time.temporal.ChronoUnit.SECONDS;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ActivityLogMapperTest {

	private final ActivityLogMapper activityLogMapper = new ActivityLogMapper(
			JsonMapper.builder()
					.findAndAddModules()
					.build()
	);
	
	@Test
	void shouldMap() {
		final ActivityLog activityLog = activityLogMapper.map(new ActivityTest());
		
		assertTrue(Instant.now().minus(1, SECONDS).isBefore(activityLog.getTimestamp()));
		assertEquals(REQUEST, activityLog.getType());
		assertEquals(1, activityLog.getContext().size());
		assertEquals("context", activityLog.getContext().get("context"));
	}
	
	static class ActivityTest extends Activity {
		
		private final String context = "context";
		
		public ActivityTest() {
			super(REQUEST);
		}
		
		public String getContext() {
			return context;
		}
	}
}