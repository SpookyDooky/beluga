package com.beluga.mapper.task.configuration.extraction_configuration.description_list;

import com.beluga.execution.model.task.extraction_configuration.description_list.DescriptionListExtractionConfiguration;
import com.beluga.model.job_definition.configuration.scraping_configuration.extraction_definition.description_list.DescriptionListExtractionDefinition;
import org.mapstruct.Mapper;

import static org.mapstruct.InjectionStrategy.CONSTRUCTOR;
import static org.mapstruct.MappingConstants.ComponentModel.SPRING;

@Mapper(
        componentModel = SPRING,
        injectionStrategy = CONSTRUCTOR,
        uses = {
                DescriptionListExtractionDataPointConfigurationMapper.class
        }
)
public interface DescriptionListExtractionConfigurationMapper {

    DescriptionListExtractionConfiguration map(DescriptionListExtractionDefinition definition);
}
