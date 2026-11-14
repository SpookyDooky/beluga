package com.beluga.properties.scraping.extraction_configuration;

import jakarta.validation.constraints.NotBlank;

/**
 * Extracts the text of the selected element.
 */
public final class TextExtractionConfigurationProperties implements ExtractionTypeProperties {

    /**
     * CSS selector, relative to the item, of the element to extract from.
     */
    @NotBlank
    private String selector;

    /**
     * Name of the property in the results.
     */
    @NotBlank
    private String field;

    public String getSelector() {
        return selector;
    }

    public void setSelector(final String selector) {
        this.selector = selector;
    }

    public String getField() {
        return field;
    }

    public void setField(final String field) {
        this.field = field;
    }
}
