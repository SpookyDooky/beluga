package com.beluga.model.job_definition.configuration.scraping_configuration.extraction_definition.text;

import com.beluga.model.job_definition.configuration.scraping_configuration.extraction_definition.ExtractionDefinition;
import jakarta.persistence.Entity;

import static com.beluga.model.job_definition.configuration.scraping_configuration.ExtractionType.TEXT;

@Entity
public class TextExtractionDefinition extends ExtractionDefinition {

    private String field;

    public TextExtractionDefinition() {
        super(TEXT);
    }

    public String getField() {
        return field;
    }

    public void setField(String field) {
        this.field = field;
    }
}
