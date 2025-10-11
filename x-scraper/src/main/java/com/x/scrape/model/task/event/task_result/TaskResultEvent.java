package com.x.scrape.model.task.event.task_result;

import com.x.scrape.model.event.storable.Storable;
import com.x.scrape.model.task.Task;
import com.x.scrape.model.task.event.TaskEvent;
import com.x.scrape.model.event.storable.payload.Payload;

import java.util.UUID;

/**
 * Event that represents some piece of data coming from task execution.
 * This might not include all data, as not all data can always be retrieved at once.
 */
public class TaskResultEvent extends TaskEvent
		implements Storable {
	
	/**
	 * Provides a hint on how this data should be stored.
	 */
	private final StorageHint storageHint;
	
	/**
	 * Payload holding the actual data of the event, payload can be anything.
	 */
	private final Payload<?> payload;
	
	/**
	 * Creates a {@link TaskResultEvent}.
	 *
	 * @param jobId    the {@link UUID} of the job this task result belongs to.
	 * @param taskId   the {@link UUID} of the task this result belongs to.
	 * @param storageHint a hint on how the data should be stored.
	 * @param payload  the payload.
	 * @throws NullPointerException thrown when the payload is null.
	 */
	public TaskResultEvent(final UUID jobId,
	                       final UUID taskId,
	                       final StorageHint storageHint,
	                       final Payload<?> payload) {
		super(jobId, taskId);
		this.storageHint = storageHint;
		this.payload = payload;
	}
	
	public static TaskResultEvent of(final Task task,
									 final StorageHint storageHint,
	                                 final Payload<?> payload) {
		validateNotNull("task", task);
		validateNotNull("storageHint", storageHint);
		validateNotNull("payload", payload);
		
		return new TaskResultEvent(
				task.getJob().getId(),
				task.getId(),
				storageHint,
				payload
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
	
	/**
	 * Returns the payload containing the scraped data.
	 *
	 * @return the {@link Payload}
	 */
	@Override
	public Payload<?> getPayload() {
		return payload;
	}
}
