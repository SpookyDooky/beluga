package com.beluga.model.job_definition.configuration.scraping_configuration.extraction_definition.attribute;

import com.beluga.model.job_definition.configuration.scraping_configuration.extraction_definition.ExtractionDefinition;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

import static com.beluga.model.job_definition.configuration.scraping_configuration.ExtractionType.ATTRIBUTE;

@Entity
@DiscriminatorValue("ATTRIBUTE")
public class AttributeExtractionDefinition extends ExtractionDefinition {

    public AttributeExtractionDefinition() {
        super(ATTRIBUTE);
    }
}
