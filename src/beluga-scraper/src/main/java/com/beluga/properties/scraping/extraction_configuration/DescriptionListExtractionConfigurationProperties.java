package com.beluga.properties.scraping.extraction_configuration;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.List;

/**
 * Extracts values from a description list, mapping each matched {@code dt} to a field in the results.
 */
public final class DescriptionListExtractionConfigurationProperties implements ExtractionTypeProperties {

    /**
     * CSS selector, relative to the item, of the description list element.
     */
    @NotBlank
    private String selector;

    /**
     * The {@code dt} values to extract and the fields to store them in.
     */
    @NotEmpty
    private List<@NotNull @Valid DescriptionListExtractionConfigurationDataPointProperties> dataPoints;

    public String getSelector() {
        return selector;
    }

    public void setSelector(final String selector) {
        this.selector = selector;
    }

    public List<DescriptionListExtractionConfigurationDataPointProperties> getDataPoints() {
        return dataPoints;
    }

    public void setDataPoints(final List<DescriptionListExtractionConfigurationDataPointProperties> dataPoints) {
        this.dataPoints = dataPoints;
    }
}
