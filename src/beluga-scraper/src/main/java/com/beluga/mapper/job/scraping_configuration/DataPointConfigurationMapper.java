package com.beluga.mapper.job.scraping_configuration;

import com.beluga.api.job.dto.read.ReadDataPointConfigurationDto;
import com.beluga.api.job.dto.write.WriteDataPointConfigurationDto;
import com.beluga.model.job_definition.configuration.scraping_configuration.DataPointDefinition;
import com.beluga.properties.scraping.DataPointProperties;
import org.mapstruct.Mapper;

import static org.mapstruct.MappingConstants.ComponentModel.SPRING;

@Mapper(componentModel = SPRING)
public interface DataPointConfigurationMapper {
	
	DataPointDefinition map(DataPointProperties dataPointProperties);
	
	DataPointDefinition map(WriteDataPointConfigurationDto writeDataPointConfigurationDto);
	
	ReadDataPointConfigurationDto map(DataPointDefinition dataPointDefinition);
}
