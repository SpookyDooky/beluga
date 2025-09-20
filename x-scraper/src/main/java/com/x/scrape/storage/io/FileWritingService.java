package com.x.scrape.storage.io;

import com.x.scrape.logging.CloseableContext;
import com.x.scrape.logging.ContextLogger;
import com.x.scrape.storage.io.exception.FileWritingException;
import org.springframework.stereotype.Service;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

@Service
public class FileWritingService {
	
	private final ContextLogger logger;
	
	public FileWritingService(final ContextLogger logger) {
		this.logger = logger;
	}
	
	public void write(final String folder,
	                  final String fileName,
	                  final String content) {
		try (final CloseableContext ignored = logger.with("fileName", fileName)) {
			
			final File file = new File(folder + fileName);
			if (!file.getParentFile().exists()) {
				createFolder(file.getParentFile());
			}
			
			write(file, content);
		}
	}
	
	private void createFolder(final File file) {
			logger.info("Creating folder " + file.getAbsolutePath());
			file.mkdirs();
//			file.createNewFile();
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
