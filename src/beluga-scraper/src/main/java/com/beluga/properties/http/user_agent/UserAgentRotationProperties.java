package com.beluga.properties.http.user_agent;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import static com.beluga.properties.http.user_agent.UserAgentRotationStrategy.ROUND_ROBIN;

public class UserAgentRotationProperties {

    @NotBlank
    private String file;
    @NotNull
    private UserAgentRotationStrategy strategy = ROUND_ROBIN;

    public String getFile() {
        return file;
    }

    public void setFile(final String file) {
        this.file = file;
    }

    public UserAgentRotationStrategy getStrategy() {
        return strategy;
    }

    public void setStrategy(final UserAgentRotationStrategy strategy) {
        this.strategy = strategy;
    }
}
