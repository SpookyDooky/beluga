package com.beluga.execution.service.worker.rate_limiting;

import com.google.common.util.concurrent.RateLimiter;

import java.util.concurrent.ThreadLocalRandom;

public class JitterRateLimiter {
	
	/**
	 * This will lead to a much longer or shorter .acquire() time.
	 */
	private static final double PROBABILITY_OF_EXTREME_VARIATION = 0.05;
	
	private static final long MAX_NEGATIVE_JITTER_MS = 2_000;
	private static final long MAX_POSITIVE_JITTER_MS = 5_000;
	
	private final RateLimiter rateLimiter;
	
	private long minJitterMs;
	private long maxJitterMs;
	
	public JitterRateLimiter(final double rate) {
		this.rateLimiter = RateLimiter.create(rate);
		configureJitterRange(rate);
	}
	
	private void configureJitterRange(final double rate) {
		final double baseMs =  1.0 / rate * 1_000;
		
		minJitterMs = Math.min(MAX_NEGATIVE_JITTER_MS, (long) (0.3 * baseMs));
		maxJitterMs = Math.min(MAX_POSITIVE_JITTER_MS, (long) (0.7 * baseMs));
	}
	
	public void acquire() {
		final long acquireMs = (long) rateLimiter.acquire();
		
		final long jitterMs = ThreadLocalRandom.current().nextLong(minJitterMs, maxJitterMs);
		sleep(jitterMs);
		
		if (ThreadLocalRandom.current().nextDouble() <= PROBABILITY_OF_EXTREME_VARIATION) {
			sleep(acquireMs);
		}
	}
	
	private void sleep(final long ms) {
		try {
			Thread.sleep(ms);
		} catch (final InterruptedException e) {
			Thread.currentThread().interrupt();
		}
	}
	
	
	public double getRate() {
		return rateLimiter.getRate();
	}
}
