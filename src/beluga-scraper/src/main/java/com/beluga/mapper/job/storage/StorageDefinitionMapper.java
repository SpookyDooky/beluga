package com.beluga.mapper.job.storage;

import com.beluga.model.job_definition.configuration.storage_configuration.StorageDefinition;
import com.beluga.properties.scraping.storage.StorageProperties;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

import static org.mapstruct.MappingConstants.ComponentModel.SPRING;

@Mapper(componentModel = SPRING)
public interface StorageDefinitionMapper {
	
	StorageDefinition map(StorageProperties storageProperties);
}
