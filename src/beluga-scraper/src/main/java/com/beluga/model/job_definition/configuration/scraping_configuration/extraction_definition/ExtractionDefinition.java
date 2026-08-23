package com.beluga.model.job_definition.configuration.scraping_configuration.extraction_definition;

import com.beluga.model.job_definition.configuration.scraping_configuration.ExtractionType;
import com.beluga.model.job_definition.configuration.scraping_configuration.ScrapingDefinition;
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

    @ManyToOne
    @JoinColumn(name = "scraping_definition_id")
    private ScrapingDefinition scrapingDefinition;

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

    public ScrapingDefinition getScrapingDefinition() {
        return scrapingDefinition;
    }

    public void setScrapingDefinition(final ScrapingDefinition scrapingDefinition) {
        this.scrapingDefinition = scrapingDefinition;
    }

}
