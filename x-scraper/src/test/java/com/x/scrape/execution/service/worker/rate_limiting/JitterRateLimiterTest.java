package com.x.scrape.execution.service.worker.rate_limiting;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

class JitterRateLimiterTest {
	
	@Test
	void shouldJitterWithinBounds() {
		final double rate = 1.0;
		
		final JitterRateLimiter jitterRateLimiter = new JitterRateLimiter(rate);
		jitterRateLimiter.acquire();
		
		for (int i = 0; i < 10; i++) {
			final long startTime = System.currentTimeMillis();
			jitterRateLimiter.acquire();
			final long totalTime = System.currentTimeMillis() - startTime;
			
			assertTrue(totalTime >= 500);
			assertTrue(totalTime <= 2000);
		}
	}
}