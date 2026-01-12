package com.x.scrape.model.event.storable.payload;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;

public class ImagePayload extends Payload<InputStream> {
	
	private final byte[] bytes;
	
	public ImagePayload(final InputStream data) {
		super(data);
		
		try {
			bytes = data.readAllBytes();
		} catch (final IOException e) {
			throw new IllegalStateException("Could not read InputStream", e);
		}
	}
	
	/**
	 * Creates a new {@link InputStream} for every invocation to be thread safe.
	 *
	 * @return a {@link InputStream}.
	 */
	@Override
	public InputStream getData() {
		return new ByteArrayInputStream(Arrays.copyOf(bytes, bytes.length));
	}
}
