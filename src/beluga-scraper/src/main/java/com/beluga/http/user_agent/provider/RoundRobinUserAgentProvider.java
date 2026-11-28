package com.beluga.http.user_agent.provider;

import java.util.List;

/**
 * Provides user-agents in using round-robin.
 */
public class RoundRobinUserAgentProvider extends UserAgentProvider {

    private final List<String> userAgents;

    public RoundRobinUserAgentProvider(final List<String> userAgents) {
        this.userAgents = userAgents;
    }

    @Override
    public String getUserAgent() {
        return "";
    }
}
