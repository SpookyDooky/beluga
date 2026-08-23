package com.beluga.mapper.task.configuration.extraction_configuration.description_list;

import com.beluga.execution.model.task.extraction_configuration.description_list.DescriptionListExtractionDataPointConfiguration;
import com.beluga.model.job_definition.configuration.scraping_configuration.extraction_definition.description_list.DescriptionListExtractionDataPointDefinition;
import org.mapstruct.Mapper;

import static org.mapstruct.MappingConstants.ComponentModel.SPRING;

@Mapper(componentModel = SPRING)
public interface DescriptionListExtractionDataPointConfigurationMapper {

    DescriptionListExtractionDataPointConfiguration map(DescriptionListExtractionDataPointDefinition definition);
}
