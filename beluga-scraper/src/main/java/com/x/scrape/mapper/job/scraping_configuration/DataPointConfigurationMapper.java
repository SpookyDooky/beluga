package com.x.scrape.mapper.job.scraping_configuration;

import com.x.scrape.api.job.dto.read.ReadDataPointConfigurationDto;
import com.x.scrape.api.job.dto.write.WriteDataPointConfigurationDto;
import com.x.scrape.model.job_definition.configuration.scraping_configuration.DataPointDefinition;
import com.x.scrape.properties.scraping.DataPointProperties;
import org.mapstruct.Mapper;

import static org.mapstruct.MappingConstants.ComponentModel.SPRING;

@Mapper(componentModel = SPRING)
public interface DataPointConfigurationMapper {
	
	DataPointDefinition map(DataPointProperties dataPointProperties);
	
	DataPointDefinition map(WriteDataPointConfigurationDto writeDataPointConfigurationDto);
	
	ReadDataPointConfigurationDto map(DataPointDefinition dataPointDefinition);
}
