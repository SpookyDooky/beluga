package com.beluga.properties.scraping.extraction_configuration;

import jakarta.validation.constraints.NotBlank;

/**
 * Extracts the value of an attribute of the selected element.
 */
public final class AttributeExtractionConfigurationProperties implements ExtractionTypeProperties {

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

    /**
     * Name of the attribute to extract the value of.
     */
    @NotBlank
    private String attribute;

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

    public String getAttribute() {
        return attribute;
    }

    public void setAttribute(final String attribute) {
        this.attribute = attribute;
    }
}
