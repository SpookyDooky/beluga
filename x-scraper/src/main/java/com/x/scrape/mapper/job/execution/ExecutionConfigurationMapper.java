package com.x.scrape.mapper.job.execution;

import com.x.scrape.api.job.dto.read.ReadExecutionConfigurationDto;
import com.x.scrape.api.job.dto.write.WriteExecutionConfigurationDto;
import com.x.scrape.model.job_definition.configuration.execution_configuration.ExecutionConfiguration;
import com.x.scrape.properties.scraping.execution.ExecutionProperties;
import org.mapstruct.Mapper;

import static org.mapstruct.MappingConstants.ComponentModel.SPRING;

@Mapper(
		componentModel = SPRING
)
public interface ExecutionConfigurationMapper {
	
	ExecutionConfiguration map(ExecutionProperties executionProperties);
	
	ExecutionConfiguration map(WriteExecutionConfigurationDto dto);
	
	ReadExecutionConfigurationDto map(ExecutionConfiguration entity);
}
