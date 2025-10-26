package com.x.scrape.mapper.job.scraping_configuration;

import com.x.scrape.model.job.configuration.scraping_configuration.DataPointConfiguration;
import com.x.scrape.properties.scraping.DataPointProperties;
import org.mapstruct.Mapper;

import static org.mapstruct.MappingConstants.ComponentModel.SPRING;

@Mapper(componentModel = SPRING)
public interface DataPointConfigurationMapper {
	
	DataPointConfiguration map(DataPointProperties dataPointProperties);
}
