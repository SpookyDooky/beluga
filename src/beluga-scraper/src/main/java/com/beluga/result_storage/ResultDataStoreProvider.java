package com.beluga.result_storage;

import com.beluga.logging.CloseableContext;
import com.beluga.logging.ContextLogger;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import static com.beluga.logging.ContextKeys.RESOURCE_IDENTIFIER;

/**
 * Used for making more datastore implementations available as storage backend.
 * This class is specifically focused on the storage of scraping results.
 */
public abstract class ResultDataStoreProvider {
	
	protected final ContextLogger logger;
	
	protected ResultDataStoreProvider(final ContextLogger logger) {
		this.logger = logger;
	}
	
	public void save(final String resourceIdentifier,
	                 final InputStream fileContent) {
		try (
				final CloseableContext ignored = logger.with(RESOURCE_IDENTIFIER, resourceIdentifier);
				final ByteArrayOutputStream outputStream = new ByteArrayOutputStream()
		) {
			final byte[] buffer = new byte[8192];
			
			int n = 0;
			while (-1 != (n = fileContent.read(buffer))) {
				outputStream.write(buffer, 0, n);
			}
			
			fileContent.close();
			save(resourceIdentifier, outputStream.toByteArray());
		} catch (final IOException e) {
			throw new IllegalStateException("Could not read file content.", e);
		}
	}
	
	public abstract void save(String resourceIdentifier, byte[] fileContent);
	
	/**
	 * Retrieves a result file.
	 *
	 * @param resourceIdentifier the identifier of a file.
	 * @return raw content of the file in bytes.
	 */
	public abstract byte[] retrieve(String resourceIdentifier);
}
