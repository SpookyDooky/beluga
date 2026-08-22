package com.beluga.api.job.dto.write.extraction_configuration.description_list;

import com.beluga.api.job.dto.write.extraction_configuration.WriteExtractionConfigurationDto;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;

import java.util.List;

import static com.beluga.model.job_definition.configuration.scraping_configuration.ExtractionType.DESCRIPTION_LIST;

public class WriteDescriptionListExtractionConfigurationDto extends WriteExtractionConfigurationDto {

    @NotBlank
    private String selector;
    @NotEmpty
    private List<@Valid WriteDescriptionListExtractionDataPointConfigurationDto> dataPoints;

    public WriteDescriptionListExtractionConfigurationDto() {
        super(DESCRIPTION_LIST);
    }

    public String getSelector() {
        return selector;
    }

    public void setSelector(final String selector) {
        this.selector = selector;
    }

    public List<WriteDescriptionListExtractionDataPointConfigurationDto> getDataPoints() {
        return dataPoints;
    }

    public void setDataPoints(final List<WriteDescriptionListExtractionDataPointConfigurationDto> dataPoints) {
        this.dataPoints = dataPoints;
    }
}
