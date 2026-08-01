package com.beluga.model.job_definition.configuration.scraping_configuration.extraction_definition;

import com.beluga.model.job_definition.configuration.scraping_configuration.ExtractionType;
import jakarta.persistence.*;

import static jakarta.persistence.GenerationType.IDENTITY;
import static jakarta.persistence.InheritanceType.JOINED;

@Entity
@Inheritance(strategy = JOINED)
public abstract class ExtractionDefinition {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    private final ExtractionType type;

    protected ExtractionDefinition(final ExtractionType type) {
        this.type = type;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ExtractionType getType() {
        return type;
    }
}
