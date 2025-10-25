package com.x.scrape.mapper.job.execution;

import com.x.scrape.model.job.execution.ExecutionConfiguration;
import com.x.scrape.properties.scraping.execution.ExecutionProperties;
import org.mapstruct.Mapper;

import static org.mapstruct.MappingConstants.ComponentModel.SPRING;

@Mapper(
		componentModel = SPRING
)
public interface ExecutionConfigurationMapper {
	
	ExecutionConfiguration map(ExecutionProperties executionProperties);
}
