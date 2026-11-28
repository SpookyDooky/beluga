package com.beluga.http.user_agent.provider;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class FixedUserAgentProviderTest {

    @Test
    void shouldGetUserAgent() {
        final String userAgent = "user-agent";
        final FixedUserAgentProvider userAgentProvider = new FixedUserAgentProvider(userAgent);

        assertEquals(userAgent, userAgentProvider.getUserAgent());
    }

    @Test
    void shouldGetSameUserAgentOnEveryCall() {
        final String userAgent = "user-agent";
        final FixedUserAgentProvider userAgentProvider = new FixedUserAgentProvider(userAgent);

        assertEquals(userAgent, userAgentProvider.getUserAgent());
        assertEquals(userAgent, userAgentProvider.getUserAgent());
    }
}
