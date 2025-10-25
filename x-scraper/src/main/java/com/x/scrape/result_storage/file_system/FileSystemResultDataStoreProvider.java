package com.x.scrape.result_storage.file_system;

import com.x.scrape.logging.CloseableContext;
import com.x.scrape.logging.ContextLogger;
import com.x.scrape.result_storage.ResultDataStoreProvider;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Path;

import static com.x.scrape.logging.ContextKeys.FILE_NAME;

@Service
@ConditionalOnProperty(
		name = "x-scraper.datastore.type",
		havingValue = "FILE_SYSTEM"
)
public class FileSystemResultDataStoreProvider extends ResultDataStoreProvider {
	
	public FileSystemResultDataStoreProvider(final ContextLogger logger) {
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
}
