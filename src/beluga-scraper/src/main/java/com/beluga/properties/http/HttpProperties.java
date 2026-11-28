package com.beluga.properties.http;

import com.beluga.properties.http.user_agent.UserAgentProperties;
import com.beluga.properties.http.validation.ValidMaxBodySize;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.springframework.util.unit.DataSize;
import org.springframework.validation.annotation.Validated;

import java.time.Duration;

import static java.time.temporal.ChronoUnit.SECONDS;
import static org.springframework.util.unit.DataUnit.MEGABYTES;

@Validated
public class HttpProperties {

    @NotNull
    private Duration timeout = Duration.of(30, SECONDS);
    @NotNull
    @ValidMaxBodySize
    private DataSize maxBodySize = DataSize.of(10, MEGABYTES);

    @Valid
    private UserAgentProperties userAgent;

    public Duration getTimeout() {
        return timeout;
    }

    public void setTimeout(final Duration timeout) {
        this.timeout = timeout;
    }

    public DataSize getMaxBodySize() {
        return maxBodySize;
    }

    public long getMaxBodySizeAsBytes() {
        return maxBodySize.toBytes();
    }

    public void setMaxBodySize(final DataSize maxBodySize) {
        this.maxBodySize = maxBodySize;
    }

    public UserAgentProperties getUserAgent() {
        return userAgent;
    }

    public void setUserAgent(final UserAgentProperties userAgent) {
        this.userAgent = userAgent;
    }
}
