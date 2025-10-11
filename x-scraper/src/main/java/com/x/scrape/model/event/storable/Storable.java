package com.x.scrape.model.event.storable;

import com.x.scrape.model.task.event.task_result.StorageHint;
import com.x.scrape.model.event.storable.payload.Payload;

public interface Storable {
	StorageHint getStorageHint();
	Payload<?> getPayload();
}
