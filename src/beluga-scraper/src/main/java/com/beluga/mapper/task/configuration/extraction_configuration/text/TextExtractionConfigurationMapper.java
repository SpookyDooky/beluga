package com.beluga.mapper.task.configuration.extraction_configuration.text;

import com.beluga.execution.model.task.extraction_configuration.text.TextExtractionConfiguration;
import com.beluga.model.job_definition.configuration.scraping_configuration.extraction_definition.text.TextExtractionDefinition;
import org.mapstruct.Mapper;

import static org.mapstruct.MappingConstants.ComponentModel.SPRING;

@Mapper(componentModel = SPRING)
public interface TextExtractionConfigurationMapper {

    TextExtractionConfiguration map(TextExtractionDefinition definition);
}
