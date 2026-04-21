package com.x.scrape.activity_logging.event;

import com.x.scrape.activity_logging.model.ActivityLog;
import com.x.scrape.execution.event.task.task_result.StorageHint;
import org.junit.jupiter.api.Test;

import java.util.Collection;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;

class ActivityFlushEventTest {
	
	@Test
	void shouldCreateActivityFlushEvent() {
		final StorageHint storageHint = mock();
		final Collection<ActivityLog> activities = mock();
		
		final ActivityFlushEvent event = ActivityFlushEvent.of(storageHint, activities);
		
		assertSame(storageHint, event.getStorageHint());
		assertSame(activities, event.getPayload().getData());
	}
	
	@Test
	void shouldThrowIllegalArgumentExceptionWhenStorageHintIsNull() {
		assertThrows(
				IllegalArgumentException.class,
				() -> ActivityFlushEvent.of(null, mock())
		);
	}
	
	@Test
	void shouldThrowIllegalArgumentExceptionWhenActivitiesIsNull() {
		assertThrows(
				IllegalArgumentException.class,
				() -> ActivityFlushEvent.of(mock(), null)
		);
	}
}