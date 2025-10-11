package com.x.scrape.logging;

import org.slf4j.MDC;

import java.io.Closeable;
import java.util.Map;

public class CloseableContext implements Closeable {
	
	private final Map<String, String> previousContext = MDC.getCopyOfContextMap();
	
	public void put(final String key,
	                final String value) {
		MDC.put(key, value);
	}
	
	@Override
	public void close() {
		MDC.clear();
		
		if (!previousContext.isEmpty()) {
			previousContext.forEach(MDC::put);
		}
	}
}
