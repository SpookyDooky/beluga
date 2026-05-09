package com.beluga.activity_logging.model;

import com.beluga.activity_logging.activitiy.TaskFailedActivity;
import org.junit.jupiter.api.Test;

import static com.beluga.activity_logging.activitiy.ActivityType.TASK_FAILED;
import static org.junit.jupiter.api.Assertions.*;

class TaskFailedActivityTest {
	
	@Test
	void shouldHaveCorrectType() {
		final TaskFailedActivity activity = new TaskFailedActivity(null, null);
		assertEquals(TASK_FAILED, activity.getType());
	}
}