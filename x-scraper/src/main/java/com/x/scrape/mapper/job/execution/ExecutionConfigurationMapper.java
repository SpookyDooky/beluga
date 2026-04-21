package com.x.scrape.mapper.job.execution;

import com.x.scrape.api.job.dto.read.ReadExecutionConfigurationDto;
import com.x.scrape.api.job.dto.write.WriteExecutionConfigurationDto;
import com.x.scrape.model.job_definition.configuration.execution_configuration.ExecutionDefinition;
import com.x.scrape.properties.scraping.execution.ExecutionProperties;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

import static org.mapstruct.MappingConstants.ComponentModel.SPRING;

@Mapper(
		componentModel = SPRING
)
public interface ExecutionConfigurationMapper {
	
	ExecutionDefinition map(ExecutionProperties executionProperties);
	
	ExecutionDefinition map(WriteExecutionConfigurationDto dto);
	
	ReadExecutionConfigurationDto map(ExecutionDefinition entity);
	
	void update(WriteExecutionConfigurationDto dto, @MappingTarget ExecutionDefinition entity);
}
