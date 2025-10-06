package com.x.scrape.model.task.event.data_result;

import com.x.scrape.model.task.event.TaskEvent;
import com.x.scrape.model.task.event.data_result.data.DataPayload;

import java.util.Optional;
import java.util.UUID;

/**
 * Event that represents some piece of data coming from task execution.
 * This might not include all data, as not all data can always be retrieved at once.
 */
public class TaskDataResultEvent extends TaskEvent {
	
	/**
	 * Optional, only required in some cases such as S3 & FileSystem is used.
	 */
	private String fileName;
	
	/**
	 * Payload holding the actual data of the event, payload can be anything.
	 */
	private final DataPayload<?> payload;
	
	/**
	 * Creates a {@link TaskDataResultEvent}.
	 *
	 * @param jobId    the {@link UUID} of the job this task result belongs to.
	 * @param taskId   the {@link UUID} of the task this result belongs to.
	 * @param fileName the file name that the data should be stored in.
	 * @param payload  the payload.
	 *
	 * @throws NullPointerException thrown when the payload is null.
	 */
	public TaskDataResultEvent(final UUID jobId,
	                           final UUID taskId,
	                           final String fileName,
	                           final DataPayload<?> payload) {
		super(jobId, taskId);
		this.fileName = fileName;
		this.payload = payload;
	}
	
	/**
	 * Returns the file name.
	 *
	 * @return the file name wrapped in {@link Optional} empty if no fileName has been supplied.
	 */
	public Optional<String> getFileName() {
		return Optional.ofNullable(fileName);
	}
	
	/**
	 * Returns the payload containing the scraped data.
	 * @return the {@link DataPayload}
	 */
	public DataPayload<?> getPayload() {
		return payload;
	}
}
