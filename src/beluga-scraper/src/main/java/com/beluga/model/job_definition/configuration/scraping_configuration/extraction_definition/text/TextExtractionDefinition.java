package com.beluga.model.job_definition.configuration.scraping_configuration.extraction_definition.text;

import com.beluga.model.job_definition.configuration.scraping_configuration.extraction_definition.ExtractionDefinition;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

import static com.beluga.model.job_definition.configuration.scraping_configuration.ExtractionType.TEXT;

@Entity
@DiscriminatorValue("TEXT")
public class TextExtractionDefinition extends ExtractionDefinition {

    private String selector;
    private String field;

    public TextExtractionDefinition() {
        super(TEXT);
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
}
