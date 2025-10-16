package com.x.scrape.model.event.storable.payload;

import java.io.InputStream;

public class ImagePayload extends Payload<InputStream> {
	public ImagePayload(final InputStream data) {
		super(data);
	}
}
