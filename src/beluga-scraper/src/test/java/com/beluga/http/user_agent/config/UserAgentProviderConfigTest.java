package com.beluga.http.user_agent.config;

import com.beluga.http.user_agent.provider.FixedUserAgentProvider;
import com.beluga.http.user_agent.provider.RandomUserAgentProvider;
import com.beluga.http.user_agent.provider.RoundRobinUserAgentProvider;
import com.beluga.properties.BelugaScraperProperties;
import com.beluga.properties.http.HttpProperties;
import com.beluga.properties.http.user_agent.UserAgentProperties;
import com.beluga.properties.http.user_agent.UserAgentRotationProperties;
import org.instancio.Instancio;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.io.TempDir;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static com.beluga.properties.http.user_agent.UserAgentRotationStrategy.RANDOM;
import static com.beluga.properties.http.user_agent.UserAgentRotationStrategy.ROUND_ROBIN;
import static org.instancio.Select.field;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserAgentProviderConfigTest {

    @TempDir
    private Path directory;

    @Mock
    private BelugaScraperProperties belugaScraperProperties;

    private final UserAgentProviderConfig userAgentProviderConfig = new UserAgentProviderConfig();

    @Test
    void shouldReturnFixedUserAgentProviderWhenUserAgentIsNotConfigured() {
        final HttpProperties httpProperties = Instancio.of(HttpProperties.class)
                .ignore(field(HttpProperties::getUserAgent))
                .create();
        when(belugaScraperProperties.getHttp()).thenReturn(httpProperties);

        assertInstanceOf(FixedUserAgentProvider.class, userAgentProviderConfig.userAgentProvider(belugaScraperProperties));
    }

    @Test
    void shouldReturnFixedUserAgentProviderWhenValueAndRotationAreNotConfigured() {
        final HttpProperties httpProperties = Instancio.of(HttpProperties.class)
                .ignore(field(UserAgentProperties::getValue))
                .ignore(field(UserAgentProperties::getRotation))
                .create();
        when(belugaScraperProperties.getHttp()).thenReturn(httpProperties);

        assertInstanceOf(FixedUserAgentProvider.class, userAgentProviderConfig.userAgentProvider(belugaScraperProperties));
    }

    @Test
    void shouldReturnFixedUserAgentProviderWhenValueIsConfigured() {
        final HttpProperties httpProperties = Instancio.of(HttpProperties.class)
                .ignore(field(UserAgentProperties::getRotation))
                .create();
        when(belugaScraperProperties.getHttp()).thenReturn(httpProperties);

        assertInstanceOf(FixedUserAgentProvider.class, userAgentProviderConfig.userAgentProvider(belugaScraperProperties));
    }

    @Test
    void shouldReturnRoundRobinUserAgentProviderWhenRoundRobinRotationIsConfigured() throws IOException {
        final Path file = Files.writeString(directory.resolve("user-agents.txt"), "Agent/1.0\n");
        final HttpProperties httpProperties = Instancio.of(HttpProperties.class)
                .ignore(field(UserAgentProperties::getValue))
                .set(field(UserAgentRotationProperties::getFile), file.toString())
                .set(field(UserAgentRotationProperties::getStrategy), ROUND_ROBIN)
                .create();
        when(belugaScraperProperties.getHttp()).thenReturn(httpProperties);

        assertInstanceOf(RoundRobinUserAgentProvider.class, userAgentProviderConfig.userAgentProvider(belugaScraperProperties));
    }

    @Test
    void shouldReturnRandomUserAgentProviderWhenRandomRotationIsConfigured() throws IOException {
        final Path file = Files.writeString(directory.resolve("user-agents.txt"), "Agent/1.0\n");
        final HttpProperties httpProperties = Instancio.of(HttpProperties.class)
                .ignore(field(UserAgentProperties::getValue))
                .set(field(UserAgentRotationProperties::getFile), file.toString())
                .set(field(UserAgentRotationProperties::getStrategy), RANDOM)
                .create();
        when(belugaScraperProperties.getHttp()).thenReturn(httpProperties);

        assertInstanceOf(RandomUserAgentProvider.class, userAgentProviderConfig.userAgentProvider(belugaScraperProperties));
    }
}
