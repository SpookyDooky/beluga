package com.x.scrape.model.task.event.task_result;

import org.jetbrains.annotations.NotNull;

/**
 * This class is meant to be added to events that contain data to give a hint about how to store the data
 * this is useful in cases where the handler might be responsible for storing the data.
 */
public final class StorageHint {
	
	private final StorageType type;
	private final String fileName;
	private final String folder;
	
	private StorageHint(final String fileName,
	                    final String folder,
	                    final StorageType type) {
		this.fileName = fileName;
		this.folder = folder;
		this.type = type;
	}
	
	/**
	 * Creates a {@link StorageHint} for a file that should be stored at the root of the task result folder.
	 *
	 * @param fileName the name of the file to store the data in.
	 * @param type     the type of data that is being stored, this should not dictate the file extension.
	 * @return the constructed {@link StorageHint}.
	 * @throws IllegalArgumentException when any of the parameters are null.
	 */
	public static StorageHint of(@NotNull final String fileName,
	                             @NotNull final StorageType type) {
		return of(fileName, "/", type);
	}
	
	/**
	 * Creates a {@link StorageHint} for a file that has to be stored in a specific location.
	 *
	 * @param fileName the name of the file to store the data in.
	 * @param folder   that the file should be stored in.
	 * @param type     the type of data that is being stored, this should not dictate the file extension.
	 * @return the constructed {@link StorageHint}.
	 * @throws IllegalArgumentException when any of the parameters are null.
	 */
	public static StorageHint of(@NotNull final String fileName,
	                             @NotNull final String folder,
	                             @NotNull final StorageType type) {
		validateNotNull("fileName", fileName);
		validateNotNull("folder", folder);
		validateNotNull("type", type);
		
		return new StorageHint(fileName, folder, type);
	}
	
	private static void validateNotNull(final String propertyName,
	                                    final Object value) {
		if (value == null) {
			throw new IllegalArgumentException(propertyName + " must not be null.");
		}
	}
	
	public StorageType getType() {
		return type;
	}
	
	public String getFileName() {
		return fileName;
	}
	
	public String getFolder() {
		return folder;
	}
}
