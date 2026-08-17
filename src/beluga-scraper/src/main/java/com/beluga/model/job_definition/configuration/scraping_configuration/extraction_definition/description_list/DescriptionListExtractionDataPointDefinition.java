package com.beluga.model.job_definition.configuration.scraping_configuration.extraction_definition.description_list;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;

import static jakarta.persistence.GenerationType.IDENTITY;

@Entity
public class DescriptionListExtractionDataPointDefinition {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    private String dtValue;
    private String field;

    @ManyToOne
    private DescriptionListExtractionDefinition descriptionListExtractionDefinition;

    public Long getId() {
        return id;
    }

    public void setId(final Long id) {
        this.id = id;
    }

    public String getDtValue() {
        return dtValue;
    }

    public void setDtValue(final String dtValue) {
        this.dtValue = dtValue;
    }

    public String getField() {
        return field;
    }

    public void setField(final String field) {
        this.field = field;
    }

    public DescriptionListExtractionDefinition getDescriptionListExtractionDefinition() {
        return descriptionListExtractionDefinition;
    }

    public void setDescriptionListExtractionDefinition(final DescriptionListExtractionDefinition descriptionListExtractionDefinition) {
        this.descriptionListExtractionDefinition = descriptionListExtractionDefinition;
    }
}
