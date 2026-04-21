package com.x.scrape.model.event.storable;

import com.x.scrape.logging.ContextLoggable;
import com.x.scrape.execution.event.task.task_result.StorageHint;
import com.x.scrape.model.event.storable.payload.Payload;

public interface StorableEvent extends ContextLoggable {
	StorageHint getStorageHint();
	Payload<?> getPayload();
}
