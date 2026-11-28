package com.beluga.http.user_agent.provider;

import org.instancio.Instancio;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Function;
import java.util.stream.Collectors;

import static com.beluga.test_utils.TestReflectionUtility.getField;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class RoundRobinUserAgentProviderTest {

    @Test
    void shouldGetUserAgentsInOrder() {
        final List<String> userAgents = Instancio.ofList(String.class).size(3).create();
        final RoundRobinUserAgentProvider userAgentProvider = new RoundRobinUserAgentProvider(userAgents);

        assertEquals(userAgents.get(0), userAgentProvider.getUserAgent());
        assertEquals(userAgents.get(1), userAgentProvider.getUserAgent());
        assertEquals(userAgents.get(2), userAgentProvider.getUserAgent());
    }

    @Test
    void shouldStartOverAfterLastUserAgent() {
        final List<String> userAgents = Instancio.ofList(String.class).size(3).create();
        final RoundRobinUserAgentProvider userAgentProvider = new RoundRobinUserAgentProvider(userAgents);

        for (int i = 0; i < userAgents.size(); i++) {
            userAgentProvider.getUserAgent();
        }

        assertEquals(userAgents.get(0), userAgentProvider.getUserAgent());
        assertEquals(userAgents.get(1), userAgentProvider.getUserAgent());
    }

    @Test
    void shouldGetSameUserAgentForSingleUserAgent() {
        final List<String> userAgents = Instancio.ofList(String.class).size(1).create();
        final RoundRobinUserAgentProvider userAgentProvider = new RoundRobinUserAgentProvider(userAgents);

        assertEquals(userAgents.getFirst(), userAgentProvider.getUserAgent());
        assertEquals(userAgents.getFirst(), userAgentProvider.getUserAgent());
    }

    @Test
    void shouldStayWithinUserAgentsWhenCounterOverflows() throws IllegalAccessException {
        final List<String> userAgents = Instancio.ofList(String.class)
                .size(3)
                .create();
        final RoundRobinUserAgentProvider userAgentProvider = new RoundRobinUserAgentProvider(userAgents);

        final Field counterField = getField("counter", RoundRobinUserAgentProvider.class);
        counterField.setAccessible(true);

        final AtomicInteger counter = (AtomicInteger) counterField.get(userAgentProvider);
        counter.set(Integer.MAX_VALUE);

        assertTrue(userAgents.contains(userAgentProvider.getUserAgent()));
        assertTrue(userAgents.contains(userAgentProvider.getUserAgent()));
    }

    @Test
    void shouldUseEveryUserAgentEquallyOftenWhenCalledConcurrently() throws InterruptedException, ExecutionException {
        final List<String> userAgents = Instancio.ofList(String.class).size(3).create();
        final RoundRobinUserAgentProvider userAgentProvider = new RoundRobinUserAgentProvider(userAgents);

        final int callsPerUserAgent = 1_000;
        final List<Callable<String>> calls = new ArrayList<>();
        for (int i = 0; i < userAgents.size() * callsPerUserAgent; i++) {
            calls.add(userAgentProvider::getUserAgent);
        }

        final List<String> results = new ArrayList<>();
        try (final ExecutorService executorService = Executors.newFixedThreadPool(8)) {
            for (final Future<String> future : executorService.invokeAll(calls)) {
                results.add(future.get());
            }
        }

        final Map<String, Long> callsByUserAgent = results.stream()
                .collect(Collectors.groupingBy(
                        Function.identity(),
                        Collectors.counting()
                ));

        assertEquals(
                Map.of(
                        userAgents.get(0), (long) callsPerUserAgent,
                        userAgents.get(1), (long) callsPerUserAgent,
                        userAgents.get(2), (long) callsPerUserAgent
                ),
                callsByUserAgent
        );
    }
}
