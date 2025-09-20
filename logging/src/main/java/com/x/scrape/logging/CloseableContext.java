package com.x.scrape.logging;

import org.slf4j.MDC;

import java.io.Closeable;

public class CloseableContext implements Closeable {
	
	@Override
	public void close() {
		MDC.clear();
	}
}
