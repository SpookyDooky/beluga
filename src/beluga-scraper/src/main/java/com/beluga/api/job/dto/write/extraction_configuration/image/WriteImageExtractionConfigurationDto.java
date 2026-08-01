package com.beluga.api.job.dto.write.extraction_configuration.image;

import com.beluga.api.job.dto.write.extraction_configuration.WriteExtractionConfigurationDto;

import static com.beluga.model.job_definition.configuration.scraping_configuration.ExtractionType.IMAGE;

public class WriteImageExtractionConfigurationDto extends WriteExtractionConfigurationDto {

    public WriteImageExtractionConfigurationDto() {
        super(IMAGE);
    }
}
