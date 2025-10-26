package com.x.scrape.mapper.job;

import com.x.scrape.execution.model.Job;
import com.x.scrape.model.job_definition.JobDefinition;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import static org.mapstruct.MappingConstants.ComponentModel.SPRING;

@Mapper(
		componentModel = SPRING
)
public interface JobMapper {
	
	@Mapping(target = "jobDefinitionId", source = "id")
	@Mapping(target = "jobName", source = "name")
	@Mapping(target = "scrapingConfiguration", source = "jobConfiguration.scrapingConfiguration")
	@Mapping(target = "storageConfiguration", source = "jobConfiguration.storageConfiguration")
	@Mapping(target = "executionConfiguration", source = "jobConfiguration.executionConfiguration")
	@Mapping(target = "urlConfiguration", source = "jobConfiguration.urlConfiguration")
	Job map(JobDefinition jobDefinition);
}
