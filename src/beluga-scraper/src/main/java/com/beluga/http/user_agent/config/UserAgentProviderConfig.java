package com.beluga.http.user_agent.config;

import com.beluga.http.user_agent.provider.FixedUserAgentProvider;
import com.beluga.http.user_agent.provider.RandomUserAgentProvider;
import com.beluga.http.user_agent.provider.RoundRobinUserAgentProvider;
import com.beluga.http.user_agent.provider.UserAgentProvider;
import com.beluga.properties.BelugaScraperProperties;
import com.beluga.properties.http.user_agent.UserAgentProperties;
import com.beluga.properties.http.user_agent.UserAgentRotationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import static java.util.function.Predicate.not;

@Configuration
public class UserAgentProviderConfig {

    private static final String DEFAULT_USER_AGENT = "Beluga/1.0";

    @Bean
    public UserAgentProvider userAgentProvider(final BelugaScraperProperties properties) {
        final UserAgentProperties userAgentProperties = properties.getHttp().getUserAgent();

        if (!hasCustomUserAgentConfiguration(userAgentProperties)) {
            return new FixedUserAgentProvider(DEFAULT_USER_AGENT);
        }

        if (userAgentProperties.getValue() != null) {
            return new FixedUserAgentProvider(userAgentProperties.getValue());
        }

        final UserAgentRotationProperties rotationProperties = userAgentProperties.getRotation();
        final List<String> userAgents = readUserAgents(Path.of(rotationProperties.getFile()));

        return switch (rotationProperties.getStrategy()) {
            case ROUND_ROBIN -> new RoundRobinUserAgentProvider(userAgents);
            case RANDOM -> new RandomUserAgentProvider(userAgents);
        };
    }

    private boolean hasCustomUserAgentConfiguration(final UserAgentProperties userAgentProperties) {
        return userAgentProperties != null &&
                (userAgentProperties.getValue() != null || userAgentProperties.getRotation() != null);
    }

    /**
     * Reads the user agents from the file, one per line. Surrounding whitespace is trimmed and blank lines are ignored.
     * The file has already been validated to exist and contain at least one user agent.
     */
    private List<String> readUserAgents(final Path file) {
        try {
            return Files.readAllLines(file).stream()
                    .map(String::strip)
                    .filter(not(String::isBlank))
                    .toList();
        } catch (final IOException e) {
            throw new IllegalStateException("Failed to read the user agent file " + file + ".", e);
        }
    }
}
