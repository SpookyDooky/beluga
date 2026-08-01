package com.beluga.model.job_definition.configuration.scraping_configuration.extraction_definition.image;

import com.beluga.model.job_definition.configuration.scraping_configuration.extraction_definition.ExtractionDefinition;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

import static com.beluga.model.job_definition.configuration.scraping_configuration.ExtractionType.IMAGE;

@Entity
@DiscriminatorValue("IMAGE")
public class ImageExtractionDefinition extends ExtractionDefinition {

    public ImageExtractionDefinition() {
        super(IMAGE);
    }
}
