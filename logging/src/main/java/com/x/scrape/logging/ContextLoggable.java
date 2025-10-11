package com.x.scrape.logging;

import java.util.Map;

public interface ContextLoggable {
	
	Map<String, String> loggingContext();
}
