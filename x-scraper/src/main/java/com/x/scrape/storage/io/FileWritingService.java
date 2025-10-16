package com.x.scrape.storage.io;

import com.x.scrape.logging.CloseableContext;
import com.x.scrape.logging.ContextLogger;
import com.x.scrape.storage.io.exception.FileWritingException;
import org.springframework.stereotype.Service;

import java.io.*;

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
			
			final File file = new File(folder + "/" + fileName);
			if (!file.getParentFile().exists()) {
				createFolder(file.getParentFile());
			}
			
			write(file, content);
		}
	}
	
	public void write(final String folder,
	                  final String fileName,
	                  final InputStream inputStream) {
		try (final CloseableContext ignored = logger.with("fileName", fileName)) {
			final File file = new File(folder + "/" +  fileName);
			if (!file.getParentFile().exists()) {
				createFolder(file.getParentFile());
			}
			
			final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
			final byte[] buffer = new byte[8192];
			
			int n = 0;
			while (-1 != (n = inputStream.read(buffer))) {
				outputStream.write(buffer, 0, n);
			}
			
			final FileOutputStream fileOutputStream = new FileOutputStream(file);
			fileOutputStream.write(outputStream.toByteArray());
			fileOutputStream.close();
			
			outputStream.close();
			inputStream.close();
		} catch (final IOException e) {
			throw new IllegalStateException("Could not read input-stream", e);
		}
	}
	
	private void createFolder(final File file){
		logger.info("Creating folder " + file.getAbsolutePath());
		file.mkdirs();
	}
	
	private void write(final File file,
	                   final String content) {
		try (final BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
			writer.write(content);
		} catch (final IOException e) {
			throw new FileWritingException("Failed to write content to " + file.getAbsolutePath() + ".", e);
		}
		
		logger.info("Successfully saved content");
	}
}
