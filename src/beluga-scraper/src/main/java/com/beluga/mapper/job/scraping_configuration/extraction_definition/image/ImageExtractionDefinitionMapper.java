package com.beluga.mapper.job.scraping_configuration.extraction_definition.image;

import com.beluga.api.job.dto.read.extraction_configuration.image.ReadImageExtractionConfigurationDto;
import com.beluga.api.job.dto.write.extraction_configuration.image.WriteImageExtractionConfigurationDto;
import com.beluga.model.job_definition.configuration.scraping_configuration.extraction_definition.image.ImageExtractionDefinition;
import org.mapstruct.Mapper;

import static org.mapstruct.MappingConstants.ComponentModel.SPRING;

@Mapper(componentModel = SPRING)
public interface ImageExtractionDefinitionMapper {

    ImageExtractionDefinition map(WriteImageExtractionConfigurationDto dto);

    ReadImageExtractionConfigurationDto map(ImageExtractionDefinition extractionDefinition);
}
