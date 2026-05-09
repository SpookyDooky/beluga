package com.beluga.execution.event.task.task_result;

import org.jetbrains.annotations.NotNull;

import java.nio.file.Path;

/**
 * This class is meant to be added to events that contain data to give a hint about how to store the data
 * this is useful in cases where the handler might be responsible for storing the data.
 */
public final class StorageHint {
	
	private final String fileName;
	private final String folder;
	
	private StorageHint(final String fileName,
	                    final String folder) {
		this.fileName = fileName;
		this.folder = folder;
	}
	
	/**
	 * Creates a {@link StorageHint} for a file that should be stored at the root of the task result folder.
	 *
	 * @param fileName the name of the file to store the data in.
	 * @return the constructed {@link StorageHint}.
	 * @throws IllegalArgumentException when any of the parameters are null.
	 */
	public static StorageHint of(@NotNull final String fileName) {
		return of(fileName, "/");
	}
	
	/**
	 * Creates a {@link StorageHint} for a file that has to be stored in a specific location.
	 *
	 * @param fileName the name of the file to store the data in.
	 * @param folder   that the file should be stored in.
	 * @return the constructed {@link StorageHint}.
	 * @throws IllegalArgumentException when any of the parameters are null.
	 */
	public static StorageHint of(@NotNull final String fileName,
	                             @NotNull final String folder) {
		validateNotNull("fileName", fileName);
		validateNotNull("folder", folder);
		
		return new StorageHint(fileName, folder);
	}
	
	private static void validateNotNull(final String propertyName,
	                                    final Object value) {
		if (value == null) {
			throw new IllegalArgumentException(propertyName + " must not be null.");
		}
	}
	
	public String getFileName() {
		return fileName;
	}
	
	public String getFolder() {
		return folder;
	}
	
	public Path getPath() {
		return Path.of(folder + "/" + fileName);
	}
}
