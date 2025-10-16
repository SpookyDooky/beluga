package com.x.scrape.model.event.storable;

import com.x.scrape.logging.ContextLoggable;
import com.x.scrape.model.task.event.task_result.StorageHint;
import com.x.scrape.model.event.storable.payload.Payload;

public interface StorableEvent extends ContextLoggable {
	StorageHint getStorageHint();
	Payload<?> getPayload();
}
