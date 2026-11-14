package com.beluga.properties.scraping.extraction_configuration;

/**
 * Properties for a single extraction type. Exactly one of these is configured per {@link ExtractionConfigurationProperties}.
 * <p>
 * Sealed so that code dispatching on the extraction type gets a compile-time error when a type is added.
 */
public sealed interface ExtractionTypeProperties permits
        AttributeExtractionConfigurationProperties,
        DescriptionListExtractionConfigurationProperties,
        HtmlExtractionConfigurationProperties,
        ImageExtractionConfigurationProperties,
        TextExtractionConfigurationProperties {
}
