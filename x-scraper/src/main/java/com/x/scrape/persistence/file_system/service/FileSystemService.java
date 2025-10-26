package com.x.scrape.persistence.file_system.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.x.scrape.persistence.shared.service.EntityIdSetterService;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Optional;

/**
 * Template for any persistence service that deals with the file system. It mainly
 * offers methods that help with file system operations.
 */
public abstract class FileSystemService {
	
	protected final ObjectMapper objectMapper;
	protected final EntityIdSetterService entityIdSetterService;
	
	protected FileSystemService(final ObjectMapper objectMapper,
	                            final EntityIdSetterService entityIdSetterService) {
		this.objectMapper = objectMapper;
		this.entityIdSetterService = entityIdSetterService;
	}
	
	/**
	 * Retrieves a file by a path.
	 *
	 * @param filePath path of the file to find.
	 * @return optional containing the file if it was found, otherwise empty.
	 */
	protected Optional<File> get(final Path filePath) {
		final File file = filePath.toFile();
		
		if (file.exists()) {
			return Optional.of(file);
		}
		
		return Optional.empty();
	}
	
	/**
	 * Reads a file and converts its contents to a POJO.
	 *
	 * @param file  the file to read.
	 * @param clazz the class to convert the content into.
	 * @param <T>   return type.
	 * @return an instance of the class based on the file content.
	 */
	protected <T> T readFileAs(final File file,
	                           final Class<T> clazz) {
		final String fileContent = readFile(file);
		try {
			return objectMapper.readValue(fileContent, clazz);
		} catch (final JsonProcessingException e) {
			throw new IllegalArgumentException(e);
		}
	}
	
	private String readFile(final File file) {
		try {
			return Files.readString(file.toPath());
		} catch (final IOException e) {
			throw new IllegalStateException("Could not read file " + file.toPath(), e);
		}
	}
	
	/**
	 * Persists content to the file system.
	 *
	 * @param content  the content that should be persisted.
	 * @param filePath the path where the content should be persisted.
	 * @param <T>      type of the content to persist.
	 * @return the persisted instance.
	 */
	protected <T> T save(final T content,
	                     final Path filePath) {
		final String jsonContent = toJson(content);
		final File file = filePath.toFile();
		
		entityIdSetterService.setIds(content);
		
		try {
			if (!file.exists()) {
				if (!file.getParentFile().exists()) {
					file.getParentFile().mkdirs();
				}
				file.createNewFile();
			}
			
			try (final BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
				writer.write(jsonContent);
				return content;
			}
		} catch (final IOException e) {
			throw new IllegalStateException("Failed to write " + content.getClass().getSimpleName() + " to " + filePath, e);
		}
	}
	
	private <T> String toJson(final T content) {
		try {
			return objectMapper.writeValueAsString(content);
		} catch (final JsonProcessingException e) {
			throw new IllegalStateException("Could not serialize " + content.getClass().getSimpleName(), e);
		}
	}
}
