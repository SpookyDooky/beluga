package com.beluga.http.user_agent.provider;

import org.instancio.Instancio;
import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class RandomUserAgentProviderTest {

    @Test
    void shouldGetUserAgentFromUserAgents() {
        final List<String> userAgents = Instancio.ofList(String.class)
                .size(3)
                .create();
        final RandomUserAgentProvider userAgentProvider = new RandomUserAgentProvider(userAgents);

        for (int i = 0; i < 100; i++) {
            assertTrue(userAgents.contains(userAgentProvider.getUserAgent()));
        }
    }

    @Test
    void shouldEventuallyGetEveryUserAgent() {
        final List<String> userAgents = Instancio.ofList(String.class)
                .size(3)
                .create();
        final RandomUserAgentProvider userAgentProvider = new RandomUserAgentProvider(userAgents);

        final Set<String> results = new HashSet<>();
        for (int i = 0; i < 1_000; i++) {
            results.add(userAgentProvider.getUserAgent());
        }

        assertEquals(Set.copyOf(userAgents), results);
    }

    @Test
    void shouldGetSameUserAgentForSingleUserAgent() {
        final List<String> userAgents = Instancio.ofList(String.class)
                .size(1)
                .create();
        final RandomUserAgentProvider userAgentProvider = new RandomUserAgentProvider(userAgents);

        for (int i = 0; i < 3; i++) {
            assertEquals(userAgents.getFirst(), userAgentProvider.getUserAgent());
        }
    }
}
