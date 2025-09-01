package com.x.scrape.storage.io.exception;

public class FileWritingException extends RuntimeException {
	
	public FileWritingException(final String message,
	                            final Throwable cause) {
		super(message, cause);
	}
}
