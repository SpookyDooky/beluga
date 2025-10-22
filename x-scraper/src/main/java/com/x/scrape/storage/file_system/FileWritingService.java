package com.x.scrape.storage.file_system;

import com.x.scrape.logging.CloseableContext;
import com.x.scrape.logging.ContextLogger;
import com.x.scrape.storage.DataStoreProvider;
import com.x.scrape.storage.file_system.exception.FileWritingException;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

import java.io.*;
import java.nio.file.Path;

import static com.x.scrape.logging.ContextKeys.FILE_NAME;

@Service
@ConditionalOnProperty(
		name = "x-scraper.datastore.type",
		havingValue = "FILE_SYSTEM"
)
public class FileWritingService extends DataStoreProvider {
	
	public FileWritingService(final ContextLogger logger) {
		super(logger);
	}
	
	@Override
	public void save(final Path filePath,
	                 final byte[] fileContent) {
		try (final CloseableContext ignored = logger.with(FILE_NAME, filePath.getFileName().toString())) {
			final File file = filePath.toFile();
			write(file, fileContent);
		}
	}
	
	private void write(final File file,
	                   final byte[] bytes) {
		if (!file.getParentFile().exists()) {
			file.getParentFile().mkdirs();
		}
		
		try (final FileOutputStream fileOutputStream = new FileOutputStream(file)){
			fileOutputStream.write(bytes);
		} catch (final IOException e) {
			throw new RuntimeException(e);
		}
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
			final File file = new File(folder + "/" + fileName);
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
	
	private void createFolder(final File file) {
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
