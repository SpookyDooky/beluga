package com.x.scrape.activity_logging.model;

import org.junit.jupiter.api.Test;

import java.util.Map;
import java.util.UUID;

import static com.x.scrape.logging.ContextKeys.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

class ActivityContextTest {
	
	@Test
	void shouldGetJobId() {
		final Long jobId = 123L;
		final Map<String, String> input = Map.of(
				JOB_EXECUTION_ID, jobId.toString()
		);
		
		final ActivityContext context = new ActivityContext(input);
		
		assertEquals(jobId, context.getJobId());
	}
	
	@Test
	void shouldGetTaskId() {
		final UUID taskId = UUID.randomUUID();
		final Map<String, String> input = Map.of(
				TASK_ID, taskId.toString()
		);
		
		final ActivityContext context = new ActivityContext(input);
		
		assertEquals(taskId, context.getTaskId());
	}
}