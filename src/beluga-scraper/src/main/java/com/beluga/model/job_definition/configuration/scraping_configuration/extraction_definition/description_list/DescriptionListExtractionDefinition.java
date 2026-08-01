package com.beluga.model.job_definition.configuration.scraping_configuration.extraction_definition.description_list;

import com.beluga.model.job_definition.configuration.scraping_configuration.extraction_definition.ExtractionDefinition;
import jakarta.persistence.Entity;

import static com.beluga.model.job_definition.configuration.scraping_configuration.ExtractionType.DESCRIPTION_LIST;

@Entity
public class DescriptionListExtractionDefinition extends ExtractionDefinition {

    protected DescriptionListExtractionDefinition() {
        super(DESCRIPTION_LIST);
    }
}
