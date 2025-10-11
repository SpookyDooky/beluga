package com.x.scrape.model.event.storable.payload;

/**
 * This payload supports a wide array of being able to store payload.
 * Including things such as collections with objects, making it possible to save a json array.
 */
public class JsonPayload extends Payload<Object> {
	public JsonPayload(final Object data) {
		super(data);
	}
}
