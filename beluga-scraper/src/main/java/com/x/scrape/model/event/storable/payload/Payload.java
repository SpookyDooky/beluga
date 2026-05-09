package com.x.scrape.model.event.storable.payload;

public class Payload<T> {
	
	protected final T data;
	
	public Payload(final T data) {
		this.data = data;
	}
	
	public T getData() {
		return data;
	}
}
