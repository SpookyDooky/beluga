package com.beluga.model.job_definition.configuration.scraping_configuration.extraction_definition.attribute;

import com.beluga.model.job_definition.configuration.scraping_configuration.extraction_definition.ExtractionDefinition;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

import static com.beluga.model.job_definition.configuration.scraping_configuration.ExtractionType.ATTRIBUTE;

@Entity
@DiscriminatorValue("ATTRIBUTE")
public class AttributeExtractionDefinition extends ExtractionDefinition {

    private String selector;
    private String field;
    private String attribute;

    public AttributeExtractionDefinition() {
        super(ATTRIBUTE);
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
}
