package com.beluga.http.user_agent.provider;

/**
 * Provides a fixed user-agent.
 */
public class FixedUserAgentProvider extends UserAgentProvider {

    private final String userAgent;

    public FixedUserAgentProvider(final String userAgent) {
        this.userAgent = userAgent;
    }

    @Override
    public String getUserAgent() {
        return userAgent;
    }
}
