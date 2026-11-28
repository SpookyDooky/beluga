package com.beluga.http.user_agent.provider;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Provides user-agents at random.
 */
public class RandomUserAgentProvider extends UserAgentProvider {

    private final List<String> userAgents;

    public RandomUserAgentProvider(final List<String> userAgents) {
        this.userAgents = userAgents;
    }

    @Override
    public String getUserAgent() {
        return userAgents.get(ThreadLocalRandom.current().nextInt(userAgents.size()));
    }
}
