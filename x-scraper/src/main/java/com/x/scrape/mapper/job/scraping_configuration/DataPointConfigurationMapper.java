package com.x.scrape.mapper.job.scraping_configuration;

import com.x.scrape.api.job.dto.write.WriteDataPointConfigurationDto;
import com.x.scrape.model.job_definition.configuration.scraping_configuration.DataPointConfiguration;
import com.x.scrape.properties.scraping.DataPointProperties;
import org.mapstruct.Mapper;

import static org.mapstruct.MappingConstants.ComponentModel.SPRING;

@Mapper(componentModel = SPRING)
public interface DataPointConfigurationMapper {
	
	DataPointConfiguration map(DataPointProperties dataPointProperties);
	
	DataPointConfiguration map(WriteDataPointConfigurationDto writeDataPointConfigurationDto);
}
