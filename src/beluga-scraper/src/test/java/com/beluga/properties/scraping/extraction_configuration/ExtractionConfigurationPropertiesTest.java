package com.beluga.properties.scraping.extraction_configuration;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class ExtractionConfigurationPropertiesTest {

    @Test
    void shouldReturnConfiguredExtractionType() {
        final ExtractionConfigurationProperties properties = new ExtractionConfigurationProperties();
        final ImageExtractionConfigurationProperties image = new ImageExtractionConfigurationProperties();
        properties.setImage(image);

        assertThat(properties.configuredExtractionType()).isSameAs(image);
    }

    @Test
    void shouldThrowWhenNoExtractionTypeIsConfigured() {
        final ExtractionConfigurationProperties properties = new ExtractionConfigurationProperties();

        assertThatThrownBy(properties::configuredExtractionType)
                .isInstanceOf(IllegalStateException.class)
                .hasMessage("Expected exactly one extraction type to be configured, but found 0");
    }

    @Test
    void shouldThrowWhenMultipleExtractionTypesAreConfigured() {
        final ExtractionConfigurationProperties properties = new ExtractionConfigurationProperties();
        properties.setImage(new ImageExtractionConfigurationProperties());
        properties.setText(new TextExtractionConfigurationProperties());

        assertThatThrownBy(properties::configuredExtractionType)
                .isInstanceOf(IllegalStateException.class)
                .hasMessage("Expected exactly one extraction type to be configured, but found 2");
    }
}
