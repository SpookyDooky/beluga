package com.beluga.properties.scraping.extraction_configuration;

import jakarta.validation.constraints.NotBlank;

public class DescriptionListExtractionConfigurationDataPointProperties {

    /**
     * The text of the {@code dt} element to match.
     */
    @NotBlank
    private String dtValue;

    /**
     * Name of the property in the results.
     */
    @NotBlank
    private String field;

    public String getDtValue() {
        return dtValue;
    }

    public void setDtValue(final String dtValue) {
        this.dtValue = dtValue;
    }

    public String getField() {
        return field;
    }

    public void setField(final String field) {
        this.field = field;
    }
}
