package com.beluga.model.job_definition.configuration.scraping_configuration.extraction_definition.html;

import com.beluga.model.job_definition.configuration.scraping_configuration.extraction_definition.ExtractionDefinition;
import jakarta.persistence.Entity;

import static com.beluga.model.job_definition.configuration.scraping_configuration.ExtractionType.HTML;

@Entity
public class HtmlExtractionDefinition extends ExtractionDefinition {

    private String field;

    public HtmlExtractionDefinition() {
        super(HTML);
    }

    public String getField() {
        return field;
    }

    public void setField(String field) {
        this.field = field;
    }
}
