package com.beluga.mapper.job.execution;

import com.beluga.api.job.dto.read.ReadExecutionConfigurationDto;
import com.beluga.api.job.dto.write.WriteExecutionConfigurationDto;
import com.beluga.model.job_definition.configuration.execution_configuration.ExecutionDefinition;
import com.beluga.properties.scraping.execution.ExecutionProperties;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

import static org.mapstruct.MappingConstants.ComponentModel.SPRING;

@Mapper(
		componentModel = SPRING
)
public interface ExecutionDefinitionMapper {
	
	ExecutionDefinition map(ExecutionProperties executionProperties);
	
	ExecutionDefinition map(WriteExecutionConfigurationDto dto);
	
	ReadExecutionConfigurationDto map(ExecutionDefinition entity);
	
	void update(WriteExecutionConfigurationDto dto, @MappingTarget ExecutionDefinition entity);
}
