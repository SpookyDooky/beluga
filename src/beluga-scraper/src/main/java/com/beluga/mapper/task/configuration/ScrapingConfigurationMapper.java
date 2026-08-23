package com.beluga.mapper.task.configuration;

import com.beluga.execution.model.task.ScrapingConfiguration;
import com.beluga.mapper.task.configuration.extraction_configuration.ExtractionConfigurationMapper;
import com.beluga.model.job_definition.configuration.scraping_configuration.ScrapingDefinition;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import static org.mapstruct.MappingConstants.ComponentModel.SPRING;

@Mapper(
        componentModel = SPRING,
        uses = {
                ExtractionConfigurationMapper.class
        }
)
public interface ScrapingConfigurationMapper {

    @Mapping(target = "extractionConfigurations", source = "extractionDefinitions")
    ScrapingConfiguration map(ScrapingDefinition scrapingDefinition);
}
