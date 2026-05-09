package com.beluga.mapper.job;

import com.beluga.execution.model.job.Job;
import com.beluga.model.job_definition.JobDefinition;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import static org.mapstruct.MappingConstants.ComponentModel.SPRING;

@Mapper(
		componentModel = SPRING
)
public interface JobMapper {
	
	@Mapping(target = "jobDefinitionId", source = "id")
	@Mapping(target = "jobName", source = "name")
	@Mapping(target = "storageConfiguration", source = "storageDefinition")
	@Mapping(target = "executionConfiguration", source = "executionDefinition")
	Job map(JobDefinition jobDefinition);
}
