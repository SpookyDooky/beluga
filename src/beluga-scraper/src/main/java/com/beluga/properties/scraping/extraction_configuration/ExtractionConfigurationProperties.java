package com.beluga.properties.scraping.extraction_configuration;

import com.beluga.model.job_definition.configuration.scraping_configuration.ExtractionType;
import com.beluga.properties.scraping.extraction_configuration.validation.annotation.ExtractionConfiguration;
import jakarta.validation.constraints.NotNull;
import org.springframework.validation.annotation.Validated;

import java.util.List;

/**
 * An aggregate properties class containing all relevant extraction configuration properties. These will then be mapped to the correct
 * entities.
 */
@Validated
@ExtractionConfiguration
public class ExtractionConfigurationProperties {

    @NotNull
    private ExtractionType type;

    private String selector;
    private String field;
    private String attribute;

    private List<DescriptionListExtractionConfigurationDataPointProperties> dataPoints;

    public ExtractionType getType() {
        return type;
    }

    public void setType(final ExtractionType type) {
        this.type = type;
    }

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

    public List<DescriptionListExtractionConfigurationDataPointProperties> getDataPoints() {
        return dataPoints;
    }

    public void setDataPoints(final List<DescriptionListExtractionConfigurationDataPointProperties> dataPoints) {
        this.dataPoints = dataPoints;
    }
}
