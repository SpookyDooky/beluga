package com.x.scrape.activity_logging.model;

import org.junit.jupiter.api.Test;

import static com.x.scrape.activity_logging.model.ActivityType.TASK_FAILED;
import static org.junit.jupiter.api.Assertions.*;

class TaskFailedActivityTest {
	
	@Test
	void shouldHaveCorrectType() {
		final TaskFailedActivity activity = new TaskFailedActivity(null, null);
		assertEquals(TASK_FAILED, activity.getType());
	}
}