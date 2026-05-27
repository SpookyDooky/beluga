package com.beluga.activity_logging.model;

import com.beluga.activity_logging.activitiy.RequestActivity;
import org.junit.jupiter.api.Test;

import java.net.URL;

import static com.beluga.activity_logging.activitiy.ActivityType.REQUEST;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;

class RequestActivityTest {
	
	@Test
	void shouldHaveCorrectActivityType() {
		final URL url = mock();
		final RequestActivity requestActivity = new RequestActivity(url, 1L, false);
		
		assertEquals(REQUEST, requestActivity.getType());
	}
}