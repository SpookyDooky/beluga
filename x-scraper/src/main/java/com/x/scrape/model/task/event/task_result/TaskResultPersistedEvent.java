package com.x.scrape.model.task.event.task_result;

import com.x.scrape.model.event.storable.payload.Payload;
import org.springframework.context.ApplicationEvent;

// TODO - Temp fix, this needs to properly extend TaskEvent
public class TaskResultPersistedEvent extends ApplicationEvent {
	
	private final Long taskId;
	private final StorageHint storageHint;
	private final Payload<?> payload;
	
	public TaskResultPersistedEvent(final TaskResultEvent taskResultEvent) {
		super(taskResultEvent.getTaskId());
		
		this.taskId = taskResultEvent.getTaskId();
		this.storageHint = taskResultEvent.getStorageHint();
		this.payload = taskResultEvent.getPayload();
	}
	
	public Long getTaskId() {
		return taskId;
	}
	
	public StorageHint getStorageHint() {
		return storageHint;
	}
	
	public Payload<?> getPayload() {
		return payload;
	}
}
