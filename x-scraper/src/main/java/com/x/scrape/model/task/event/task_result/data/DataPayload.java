package com.x.scrape.model.task.event.task_result.data;

public class DataPayload<T> {
	
	public final T data;
	
	public DataPayload(final T data) {
		this.data = data;
	}
	
	public T getData() {
		return data;
	}
}
