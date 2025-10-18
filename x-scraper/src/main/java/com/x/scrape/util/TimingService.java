package com.x.scrape.util;

import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class TimingService {
	
	private final Map<UUID, Long> startTimes = new ConcurrentHashMap<>();
	
	public UUID start() {
		final UUID uuid = UUID.randomUUID();
		start(uuid);
		return uuid;
	}
	
	public void start(final UUID id) {
		startTimes.put(id, System.currentTimeMillis());
	}
	
	public Long stop(final UUID uuid) {
		if (!startTimes.containsKey(uuid)) {
			throw new IllegalArgumentException("No such timing context registered");
		}
		
		return System.currentTimeMillis() - startTimes.remove(uuid);
	}
}
