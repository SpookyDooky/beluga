package com.x.scrape.result_storage.file_system.exception;

public class FileCreationException extends RuntimeException {
	
	public FileCreationException(final String message,
	                             final Throwable cause) {
		super(message, cause);
	}
}
