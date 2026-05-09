package com.beluga.activity_logging.event;

import com.beluga.activity_logging.model.ActivityLog;
import com.beluga.model.event.storable.StorableEvent;
import com.beluga.model.event.storable.payload.JsonPayload;
import com.beluga.execution.event.task.task_result.StorageHint;

import java.util.Collection;
import java.util.Map;

// Activities will be stored in the database
// TODO - Remove
@Deprecated(forRemoval = true)
public class ActivityFlushEvent implements StorableEvent {
	
	/**
	 * Provides a hint on how this data should be stored.
	 */
	private final StorageHint storageHint;
	
	/**
	 * Json payload holding all the activities.
	 */
	private final JsonPayload payload;
	
	private ActivityFlushEvent(final StorageHint storageHint,
	                           final JsonPayload payload) {
		this.storageHint = storageHint;
		this.payload = payload;
	}
	
	public static ActivityFlushEvent of(final StorageHint storageHint,
	                                    final Collection<ActivityLog> activities) {
		validateNotNull("storageHint", storageHint);
		validateNotNull("activities", activities);
		
		return new ActivityFlushEvent(
				storageHint,
				new JsonPayload(activities)
		);
	}
	
	private static void validateNotNull(final String propertyName,
	                                    final Object value) {
		if (value == null) {
			throw new IllegalArgumentException(propertyName + " must not be null.");
		}
	}
	
	@Override
	public StorageHint getStorageHint() {
		return storageHint;
	}
	
	@Override
	public JsonPayload getPayload() {
		return payload;
	}
	
	@Override
	public Map<String, String> loggingContext() {
		return Map.of();
	}
}
