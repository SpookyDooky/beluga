package com.x.scrape.storage;

import com.x.scrape.logging.CloseableContext;
import com.x.scrape.logging.ContextLogger;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;

import static com.x.scrape.logging.ContextKeys.FILE_NAME;

/**
 * User for making more datastore implementations available as storage backend.
 */
public abstract class DataStoreProvider {
	
	protected final ContextLogger logger;
	
	protected DataStoreProvider(final ContextLogger logger) {
		this.logger = logger;
	}
	
	public void save(final Path filePath,
	                 final InputStream fileContent) {
		try (
				final CloseableContext ignored = logger.with(FILE_NAME, filePath.toString());
				final ByteArrayOutputStream outputStream = new ByteArrayOutputStream()
		) {
			final byte[] buffer = new byte[8192];
			
			int n = 0;
			while (-1 != (n = fileContent.read(buffer))) {
				outputStream.write(buffer, 0, n);
			}
			
			fileContent.close();
			save(filePath, outputStream.toByteArray());
		} catch (final IOException e) {
			throw new IllegalStateException("Could not read file content.", e);
		}
	}
	
	public abstract void save(Path filePath, byte[] fileContent);
}
