package com.beluga.mapper.task.configuration;

import com.beluga.execution.model.task.DataPointConfiguration;
import com.beluga.model.job_definition.configuration.scraping_configuration.DataPointDefinition;
import org.mapstruct.Mapper;

import static org.mapstruct.MappingConstants.ComponentModel.SPRING;

@Mapper(
        componentModel = SPRING
)
public interface DataPointConfigurationMapper {

    DataPointConfiguration map(DataPointDefinition dataPointDefinition);
}
