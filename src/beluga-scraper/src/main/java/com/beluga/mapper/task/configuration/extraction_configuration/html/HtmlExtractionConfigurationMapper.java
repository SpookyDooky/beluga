package com.beluga.mapper.task.configuration.extraction_configuration.html;

import com.beluga.execution.model.task.extraction_configuration.html.HtmlExtractionConfiguration;
import com.beluga.model.job_definition.configuration.scraping_configuration.extraction_definition.html.HtmlExtractionDefinition;
import org.mapstruct.Mapper;

import static org.mapstruct.MappingConstants.ComponentModel.SPRING;

@Mapper(componentModel = SPRING)
public interface HtmlExtractionConfigurationMapper {

    HtmlExtractionConfiguration map(HtmlExtractionDefinition definition);
}
