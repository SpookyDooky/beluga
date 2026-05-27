package com.beluga.util;

import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Simple services that can keep tracking of multiple timers.
 */
@Service
public class TimingService {

    private final Map<UUID, Long> startTimes = new ConcurrentHashMap<>();

    /**
     * Starts a timer.
     *
     * @return the id of the timer.
     */
    public UUID start() {
        final UUID uuid = UUID.randomUUID();
        start(uuid);
        return uuid;
    }

    /**
     * Starts a timer with a specified {@link UUID} as id.
     *
     * @param id id of timer.
     */
    public void start(final UUID id) {
        startTimes.put(id, System.currentTimeMillis());
    }

    /**
     * Stops the timer.
     *
     * @param uuid id of the timer.
     * @return passed time in ms.
     */
    public Long stop(final UUID uuid) {
        if (!startTimes.containsKey(uuid)) {
            throw new IllegalArgumentException("No such timing context registered");
        }

        return System.currentTimeMillis() - startTimes.remove(uuid);
    }
}
