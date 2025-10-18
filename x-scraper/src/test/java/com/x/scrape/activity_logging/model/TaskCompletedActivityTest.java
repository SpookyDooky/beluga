package com.x.scrape.activity_logging.model;

import org.junit.jupiter.api.Test;

import static com.x.scrape.activity_logging.model.ActivityType.TASK_COMPLETED;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;

class TaskCompletedActivityTest {
	
	@Test
	void shouldHaveCorrectType() {
		final TaskCompletedActivity activity = new TaskCompletedActivity(mock(), 123L);
		assertEquals(TASK_COMPLETED, activity.getType());
	}
}