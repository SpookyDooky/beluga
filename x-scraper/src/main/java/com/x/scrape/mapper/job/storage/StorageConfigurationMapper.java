package com.x.scrape.mapper.job.storage;

import com.x.scrape.api.job.dto.read.ReadStorageConfigurationDto;
import com.x.scrape.api.job.dto.write.WriteStorageConfigurationDto;
import com.x.scrape.model.job_definition.configuration.storage_configuration.StorageConfiguration;
import com.x.scrape.properties.scraping.storage.StorageProperties;
import org.mapstruct.Mapper;

import static org.mapstruct.MappingConstants.ComponentModel.SPRING;

@Mapper(componentModel = SPRING)
public interface StorageConfigurationMapper {
	
	StorageConfiguration map(StorageProperties storageProperties);
	
	StorageConfiguration map(WriteStorageConfigurationDto dto);
	
	ReadStorageConfigurationDto map(StorageConfiguration entity);
}
