package com.beluga.logging;

import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class ContextLoggerTest {

	@Mock
	private Logger logger;
	
	@InjectMocks
	private ContextLogger contextLogger;
	
	@Test
	void shouldTrace() {
		final String message = "message";
		
		contextLogger.trace(message);
		
		verify(logger).trace(message);
	}
}