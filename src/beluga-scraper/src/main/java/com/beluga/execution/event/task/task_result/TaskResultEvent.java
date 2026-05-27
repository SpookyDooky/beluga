package com.beluga.execution.event.task.task_result;

import com.beluga.execution.event.task.TaskEvent;
import com.beluga.execution.model.task.Task;
import com.beluga.model.event.storable.payload.Payload;

/**
 * Event that represents some piece of data coming from task execution.
 * This might not include all data, as not all data can always be retrieved at once.
 */
public class TaskResultEvent extends TaskEvent {

    private final String key;
    private final Payload<?> payload;

    /**
     * Creates a {@link TaskResultEvent}.
     *
     * @param task    the task this result is for.
     * @param key     name of the file in which the results should be stored.
     * @param payload the payload.
     * @throws NullPointerException thrown when the payload is null.
     */
    private TaskResultEvent(final Task task,
                            final String key,
                            final Payload<?> payload) {
        super(task);
        this.key = key;
        this.payload = payload;
    }

    public static TaskResultEvent of(final Task task,
                                     final String key,
                                     final Payload<?> payload) {
        validateNotNull("task", task);
        validateNotNull("key", key);
        validateNotNull("payload", payload);

        return new TaskResultEvent(
                task,
                key,
                payload
        );
    }

    private static void validateNotNull(final String propertyName,
                                        final Object value) {
        if (value == null) {
            throw new IllegalArgumentException(propertyName + " must not be null.");
        }
    }

    public String getKey() {
        return key;
    }

    public Payload<?> getPayload() {
        return payload;
    }
}
