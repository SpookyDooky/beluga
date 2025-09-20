package com.x.scrape.logging;

import org.slf4j.MDC;

import java.io.Closeable;

public class CloseableContext implements Closeable {
	
	public void put(final String key,
	                final String value) {
		MDC.put(key, value);
	}
	
	@Override
	public void close() {
		MDC.clear();
	}
}
