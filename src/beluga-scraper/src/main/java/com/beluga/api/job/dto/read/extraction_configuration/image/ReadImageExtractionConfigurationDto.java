package com.beluga.api.job.dto.read.extraction_configuration.image;

import com.beluga.api.job.dto.read.extraction_configuration.ReadExtractionConfigurationDto;

import static com.beluga.model.job_definition.configuration.scraping_configuration.ExtractionType.IMAGE;

public class ReadImageExtractionConfigurationDto extends ReadExtractionConfigurationDto {

    public ReadImageExtractionConfigurationDto() {
        super(IMAGE);
    }
}
