package com.beluga.logging;

import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.slf4j.MDC;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class ContextLoggerTest {

	@Mock
	private Logger logger;
	
	@InjectMocks
	private ContextLogger contextLogger;
	
	@AfterEach
	void tearDown() {
		MDC.clear();
	}
	
	@Test
	void shouldTrace() {
		final String message = "message";
		
		contextLogger.trace(message);
		
		verify(logger).trace(message);
	}
	
	@Test
	void shouldRemoveKeyWhenContextIsClosed() {
		try (final CloseableContext ignored = contextLogger.with("key", "value")) {
			assertEquals("value", MDC.get("key"));
		}
		
		assertNull(MDC.get("key"));
	}
	
	@Test
	void shouldKeepOuterKeyAndRemoveInnerKeyWhenInnerContextIsClosed() {
		try (final CloseableContext ignoredOuter = contextLogger.with("outer", "outerValue")) {
			try (final CloseableContext ignoredInner = contextLogger.with("inner", "innerValue")) {
				assertEquals("innerValue", MDC.get("inner"));
			}
			
			assertEquals("outerValue", MDC.get("outer"));
			assertNull(MDC.get("inner"));
		}
	}
}