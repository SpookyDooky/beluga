package com.beluga.mapper.task.configuration;

import com.beluga.execution.model.task.ScrapingConfiguration;
import com.beluga.model.job_definition.configuration.scraping_configuration.ScrapingDefinition;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import static org.mapstruct.MappingConstants.ComponentModel.SPRING;

@Mapper(
        componentModel = SPRING,
        uses = {
                DataPointConfigurationMapper.class
        }
)
public interface ScrapingConfigurationMapper {

    @Mapping(target = "dataPointConfigurations", source = "dataPointDefinitions")
    ScrapingConfiguration map(ScrapingDefinition scrapingDefinition);
}
