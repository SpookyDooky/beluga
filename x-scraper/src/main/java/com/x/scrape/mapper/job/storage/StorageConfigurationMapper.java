package com.x.scrape.mapper.job.storage;

import com.x.scrape.model.job.storage.StorageConfiguration;
import com.x.scrape.properties.scraping.storage.StorageProperties;
import org.mapstruct.Mapper;

import static org.mapstruct.MappingConstants.ComponentModel.SPRING;

@Mapper(componentModel = SPRING)
public interface StorageConfigurationMapper {
	
	StorageConfiguration map(StorageProperties storageProperties);
}
