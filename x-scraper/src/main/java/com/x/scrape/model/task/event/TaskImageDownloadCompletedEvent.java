package com.x.scrape.model.task.event;

import java.io.InputStream;
import java.util.UUID;

public class TaskImageDownloadCompletedEvent extends TaskEvent {
	
	private InputStream fileStream;
	private final String fileName;
	
	public TaskImageDownloadCompletedEvent(final UUID jobId,
	                                       final UUID taskId,
	                                       final String fileName) {
		super(jobId, taskId);
		this.fileName = fileName;
	}
	
	public InputStream getFileStream() {
		return fileStream;
	}
	
	public void setFileStream(final InputStream fileStream) {
		this.fileStream = fileStream;
	}
	
	public String getFileName() {
		return fileName;
	}
}
