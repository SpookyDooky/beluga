package com.beluga.mapper.task.configuration;

import com.beluga.model.job_definition.configuration.storage_configuration.StorageDefinition;
import org.mapstruct.Mapper;

import static org.mapstruct.MappingConstants.ComponentModel.SPRING;

@Mapper(componentModel = SPRING)
public interface StorageConfigurationMapper {

    StorageConfiguration map(StorageDefinition storageDefinition);
}
