package com.beluga.model.event.storable;

import com.beluga.logging.ContextLoggable;
import com.beluga.execution.event.task.task_result.StorageHint;
import com.beluga.model.event.storable.payload.Payload;

public interface StorableEvent extends ContextLoggable {
	String getFileName();
	Payload<?> getPayload();
}
