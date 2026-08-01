package com.beluga.model.job_definition.configuration.scraping_configuration.extraction_definition.description_list;

import com.beluga.model.job_definition.configuration.scraping_configuration.extraction_definition.ExtractionDefinition;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;

import java.util.List;

import static com.beluga.model.job_definition.configuration.scraping_configuration.ExtractionType.DESCRIPTION_LIST;
import static jakarta.persistence.CascadeType.ALL;

@Entity
@DiscriminatorValue("DESCRIPTION_LIST")
public class DescriptionListExtractionDefinition extends ExtractionDefinition {

    @OneToMany(
            cascade = ALL,
            mappedBy = "descriptionListExtractionDefinition"
    )
    private List<DescriptionListExtractionDataPointDefinition> dataPoints;

    public DescriptionListExtractionDefinition() {
        super(DESCRIPTION_LIST);
    }

    public List<DescriptionListExtractionDataPointDefinition> getDataPoints() {
        return dataPoints;
    }

    public void setDataPoints(final List<DescriptionListExtractionDataPointDefinition> dataPoints) {
        this.dataPoints = dataPoints;
        dataPoints.forEach(dataPoint -> dataPoint.setDescriptionListExtractionDefinition(this));
    }
}
