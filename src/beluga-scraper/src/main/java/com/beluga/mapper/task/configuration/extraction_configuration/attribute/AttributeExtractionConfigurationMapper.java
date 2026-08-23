package com.beluga.mapper.task.configuration.extraction_configuration.attribute;

import com.beluga.execution.model.task.extraction_configuration.attribute.AttributeExtractionConfiguration;
import com.beluga.model.job_definition.configuration.scraping_configuration.extraction_definition.attribute.AttributeExtractionDefinition;
import org.mapstruct.Mapper;

import static org.mapstruct.MappingConstants.ComponentModel.SPRING;

@Mapper(componentModel = SPRING)
public interface AttributeExtractionConfigurationMapper {

    AttributeExtractionConfiguration map(AttributeExtractionDefinition definition);
}
