package com.beluga.model.job_definition.configuration.scraping_configuration.extraction_definition;

import com.beluga.model.job_definition.configuration.scraping_configuration.ExtractionType;
import jakarta.persistence.*;

import static jakarta.persistence.EnumType.STRING;
import static jakarta.persistence.GenerationType.IDENTITY;
import static jakarta.persistence.InheritanceType.JOINED;

@Entity
@Inheritance(strategy = JOINED)
@DiscriminatorColumn(name = "type")
public abstract class ExtractionDefinition {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    @Column(insertable = false, updatable = false)
    @Enumerated(STRING)
    private final ExtractionType type;

    protected ExtractionDefinition(final ExtractionType type) {
        this.type = type;
    }

    public Long getId() {
        return id;
    }

    public void setId(final Long id) {
        this.id = id;
    }

    public ExtractionType getType() {
        return type;
    }
}
