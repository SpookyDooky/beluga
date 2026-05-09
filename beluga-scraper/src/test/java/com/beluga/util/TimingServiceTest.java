package com.beluga.util;

import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class TimingServiceTest {
	
	private final TimingService timingService = new TimingService();
	
	@Test
	void shouldStart() throws InterruptedException {
		final Set<UUID> uuids = new HashSet<>();
		for (int i = 0; i < 100; i++) {
			final UUID uuid = timingService.start();
			
			assertFalse(uuids.contains(uuid));
			uuids.add(uuid);
		}
		
		Thread.sleep(200);
		assertTrue(timingService.stop(uuids.stream().findFirst().get()) > 199);
	}
	
	@Test
	void shouldThrowExceptionWhenStop() {
		assertThrows(IllegalArgumentException.class, () -> timingService.stop(UUID.randomUUID()));
	}
}