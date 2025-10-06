package com.x.scrape.model.task.event;

import java.io.*;
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
	 * The data retrieved by the task.
	 */
	private final InputStream data;
	
	/**
	 * Creates a {@link TaskDataResultEvent}.
	 *
	 * @param jobId    the {@link UUID} of the job this task result belongs to.
	 * @param taskId   the {@link UUID} of the task this result belongs to.
	 * @param fileName the file name that the data should be stored in.
	 * @param data     the data to store.
	 */
	public TaskDataResultEvent(final UUID jobId,
	                           final UUID taskId,
	                           final String fileName,
	                           final Object data) {
		this(jobId, taskId, data);
		this.fileName = fileName;
	}
	
	/**
	 * Creates a {@link TaskDataResultEvent}.
	 *
	 * @param jobId  the {@link UUID} of the job this task result belongs to.
	 * @param taskId the {@link UUID} of the task this result belongs to.
	 * @param data   the data to store.
	 * @throws NullPointerException if the supplied data is null.
	 */
	public TaskDataResultEvent(final UUID jobId,
	                           final UUID taskId,
	                           final Object data) {
		super(jobId, taskId);
		
		if (data == null) {
			throw new NullPointerException("Data cannot be null.");
		}
		
		if (data instanceof InputStream dataInputStream) {
			this.data = dataInputStream;
		} else {
			this.data = createInputStream(data);
		}
	}
	
	/**
	 * Creates an {@link InputStream} from an {@link Object}.
	 *
	 * @param object the object to create an input-stream for.
	 * @return the object's {@link InputStream}
	 * @throws IllegalStateException if an unexpected exception occurs whilst making the object's {@link InputStream}.
	 */
	private InputStream createInputStream(final Object object) {
		try (
				final ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
				final ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
		) {
			objectOutputStream.writeObject(object);
			objectOutputStream.flush();
			
			return new ByteArrayInputStream(byteArrayOutputStream.toByteArray());
		} catch (final IOException e) {
			throw new IllegalStateException("Failed to create object output stream for data", e);
		}
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
	 * Note: The data returned may very well contain java object identifiers, as objects are read through an {@link ObjectOutputStream}.
	 * @return the data that was retrieved by the task.
	 */
	public InputStream getData() {
		return data;
	}
}
