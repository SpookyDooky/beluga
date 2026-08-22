package com.beluga.mapper.task.configuration;

import com.beluga.execution.model.task.DataPointConfiguration;
import com.beluga.mapper.task.configuration.extraction_configuration.ExtractionConfigurationMapper;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import static org.mapstruct.InjectionStrategy.CONSTRUCTOR;
import static org.mapstruct.MappingConstants.ComponentModel.SPRING;

@Mapper(
        componentModel = SPRING,
        injectionStrategy = CONSTRUCTOR,
        uses = {
                ExtractionConfigurationMapper.class
        }
)
public interface DataPointConfigurationMapper {

    @Mapping(target = "extractionConfiguration", source = "extractionDefinition")
    DataPointConfiguration map(DataPointDefinition dataPointDefinition);
}
