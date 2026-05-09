package com.x.scrape.result_storage;

import com.x.scrape.logging.CloseableContext;
import com.x.scrape.logging.ContextLogger;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;

import static com.x.scrape.logging.ContextKeys.FILE_NAME;

/**
 * Used for making more datastore implementations available as storage backend.
 * This class is specifically focused on the storage of scraping results.
 */
public abstract class ResultDataStoreProvider {
	
	protected final ContextLogger logger;
	
	protected ResultDataStoreProvider(final ContextLogger logger) {
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
	
	/**
	 * Retrieves a result file.
	 *
	 * @param filePath the {@link Path} of the file.
	 * @return raw content of the file in bytes.
	 */
	public abstract byte[] retrieve(Path filePath);
}
