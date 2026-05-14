package com.beluga.mapper.job.storage;

import com.beluga.api.job.dto.read.ReadStorageConfigurationDto;
import com.beluga.api.job.dto.write.WriteStorageConfigurationDto;
import com.beluga.model.job_definition.configuration.storage_configuration.StorageDefinition;
import com.beluga.properties.scraping.storage.StorageProperties;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

import static org.mapstruct.MappingConstants.ComponentModel.SPRING;

@Mapper(componentModel = SPRING)
public interface StorageDefinitionMapper {
	
	StorageDefinition map(StorageProperties storageProperties);
	
	StorageDefinition map(WriteStorageConfigurationDto dto);
	
	ReadStorageConfigurationDto map(StorageDefinition entity);
	
	void update(WriteStorageConfigurationDto dto, @MappingTarget StorageDefinition storageDefinition);
}
