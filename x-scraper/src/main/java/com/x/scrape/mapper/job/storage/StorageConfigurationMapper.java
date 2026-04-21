package com.x.scrape.mapper.job.storage;

import com.x.scrape.api.job.dto.read.ReadStorageConfigurationDto;
import com.x.scrape.api.job.dto.write.WriteStorageConfigurationDto;
import com.x.scrape.model.job_definition.configuration.storage_configuration.StorageDefinition;
import com.x.scrape.properties.scraping.storage.StorageProperties;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

import static org.mapstruct.MappingConstants.ComponentModel.SPRING;

@Mapper(componentModel = SPRING)
public interface StorageConfigurationMapper {
	
	StorageDefinition map(StorageProperties storageProperties);
	
	StorageDefinition map(WriteStorageConfigurationDto dto);
	
	ReadStorageConfigurationDto map(StorageDefinition entity);
	
	void update(WriteStorageConfigurationDto dto, @MappingTarget StorageDefinition storageDefinition);
}
