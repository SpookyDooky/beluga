package com.x.scrape.storage.io;

import com.x.scrape.storage.io.exception.FileCreationException;
import com.x.scrape.storage.io.exception.FileWritingException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

@Service
public class FileWritingService {
	
	private final Logger logger = LogManager.getLogger();
	
	public void write(final String folder,
	                  final String fileName,
	                  final String content) {
		final File file = new File(folder + fileName);
		
		if (!file.exists()) {
			createFile(file);
		}
		
		write(file, content);
	}
	
	private void createFile(final File file) {
		try {
			logger.info("Creating file " + file.getAbsolutePath());
			file.getParentFile().mkdirs();
			file.createNewFile();
		} catch (final IOException e) {
			logger.error("Failed to create file " + file.getAbsolutePath());
			throw new FileCreationException("Failed to create file " + file.getAbsolutePath(), e);
		}
	}
	
	private void write(final File file,
	                   final String content) {
		logger.info("Writing to " + file.getAbsolutePath());
		
		try (final BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
			writer.write(content);
		} catch (final IOException e) {
			throw new FileWritingException("Failed to write content to " + file.getAbsolutePath() + ".", e);
		}
		
		logger.info("Successfully saved content");
	}
}
