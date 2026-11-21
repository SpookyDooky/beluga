package com.beluga.properties.scraping.extraction_configuration;

import org.instancio.Instancio;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.instancio.Select.field;

class ExtractionConfigurationPropertiesTest {

    @Test
    void shouldReturnGetConfiguredExtractionType() {
        final ExtractionConfigurationProperties properties = Instancio.of(ExtractionConfigurationProperties.class)
                .ignore(field(ExtractionConfigurationProperties::getAttribute))
                .ignore(field(ExtractionConfigurationProperties::getDescriptionList))
                .ignore(field(ExtractionConfigurationProperties::getHtml))
                .ignore(field(ExtractionConfigurationProperties::getText))
                .create();

        assertThat(properties.getConfiguredExtractionType()).isSameAs(properties.getImage());
    }

    @Test
    void shouldThrowWhenNoExtractionTypeIsConfigured() {
        final ExtractionConfigurationProperties properties = Instancio.of(ExtractionConfigurationProperties.class)
                .ignore(field(ExtractionConfigurationProperties::getAttribute))
                .ignore(field(ExtractionConfigurationProperties::getDescriptionList))
                .ignore(field(ExtractionConfigurationProperties::getHtml))
                .ignore(field(ExtractionConfigurationProperties::getImage))
                .ignore(field(ExtractionConfigurationProperties::getText))
                .create();

        assertThatThrownBy(properties::getConfiguredExtractionType)
                .isInstanceOf(IllegalStateException.class)
                .hasMessage("Expected exactly one extraction type to be configured, but found 0");
    }

    @Test
    void shouldThrowWhenMultipleExtractionTypesAreConfigured() {
        final ExtractionConfigurationProperties properties = Instancio.of(ExtractionConfigurationProperties.class)
                .ignore(field(ExtractionConfigurationProperties::getAttribute))
                .ignore(field(ExtractionConfigurationProperties::getDescriptionList))
                .ignore(field(ExtractionConfigurationProperties::getHtml))
                .create();

        assertThatThrownBy(properties::getConfiguredExtractionType)
                .isInstanceOf(IllegalStateException.class)
                .hasMessage("Expected exactly one extraction type to be configured, but found 2");
    }
}
