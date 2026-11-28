package com.beluga.properties.http.user_agent;

import com.beluga.properties.http.user_agent.validation.ValidUserAgentFile;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.springframework.validation.annotation.Validated;

import static com.beluga.properties.http.user_agent.UserAgentRotationStrategy.ROUND_ROBIN;

@Validated
public class UserAgentRotationProperties {

    @NotBlank
    @ValidUserAgentFile
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
