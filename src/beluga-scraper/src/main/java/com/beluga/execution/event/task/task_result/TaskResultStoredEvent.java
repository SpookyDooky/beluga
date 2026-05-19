package com.beluga.execution.event.task.task_result;

import com.beluga.execution.event.task.TaskEvent;
import com.beluga.model.event.storable.payload.Payload;

public class TaskResultStoredEvent extends TaskEvent {

    private final String namespace;
    private final String key;
    private final Payload<?> payload;

    public TaskResultStoredEvent(final Long jobDefinitionId,
                                 final Long jobId,
                                 final Long taskId,
                                 final String namespace,
                                 final String key,
                                 final Payload<?> payload) {
        super(jobDefinitionId, jobId, taskId);
        this.namespace = namespace;
        this.key = key;
        this.payload = payload;
    }

    public String getNamespace() {
        return namespace;
    }

    public String getKey() {
        return key;
    }

    public Payload<?> getPayload() {
        return payload;
    }
}
