package com.beluga.mapper.task.configuration.extraction_configuration.image;

import com.beluga.execution.model.task.extraction_configuration.image.ImageExtractionConfiguration;
import com.beluga.model.job_definition.configuration.scraping_configuration.extraction_definition.image.ImageExtractionDefinition;
import org.mapstruct.Mapper;

import static org.mapstruct.MappingConstants.ComponentModel.SPRING;

@Mapper(componentModel = SPRING)
public interface ImageExtractionConfigurationMapper {

    ImageExtractionConfiguration map(ImageExtractionDefinition definition);
}
