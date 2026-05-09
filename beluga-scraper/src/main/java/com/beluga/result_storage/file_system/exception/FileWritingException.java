package com.beluga.result_storage.file_system.exception;

public class FileWritingException extends RuntimeException {
	
	public FileWritingException(final String message,
	                            final Throwable cause) {
		super(message, cause);
	}
}
