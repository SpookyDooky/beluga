package com.x.scrape.activity_logging.model;

import org.junit.jupiter.api.Test;

import java.net.URL;

import static com.x.scrape.activity_logging.model.ActivityType.REQUEST;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

class RequestActivityTest {
	
	@Test
	void shouldHaveCorrectActivityType() {
		final URL url = mock();
		final RequestActivity requestActivity = new RequestActivity(url, 1L, false);
		
		assertEquals(REQUEST, requestActivity.getType());
	}
}