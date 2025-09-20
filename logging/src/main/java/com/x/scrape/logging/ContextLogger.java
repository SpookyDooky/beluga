package com.x.scrape.logging;

import org.apache.logging.log4j.Logger;
import org.slf4j.MDC;

public class ContextLogger {

	private final Logger logger;
	
	public ContextLogger(final Logger logger) {
		this.logger = logger;
	}
	
	public CloseableContext with(final String... context) {
		configureContext(context);
		return new CloseableContext();
	}
	
	private void configureContext(final String... context) {
		for (int i = 0; (i + 1) < context.length; i += 2) {
			MDC.put(context[i], context[i + 1]);
		}
	}
	
	public void info(final String message,
	                 final String... context) {
		configureContext(context);
		logger.info(message);
	}
	
	public void error(final String message) {
		logger.error(message);
	}
	
	public void error(final String message,
	                  final Throwable throwable) {
		logger.error(message, throwable);
	}
}
