package com.x.scrape.mapper.task;

import com.x.scrape.model.job_definition.JobDefinition;
import com.x.scrape.model.task.Task;
import com.x.scrape.model.task.TaskDefinition;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import static org.mapstruct.MappingConstants.ComponentModel.SPRING;

@Mapper(
		componentModel = SPRING
)
public interface TaskMapper {
	
	@Mapping(target = "url", source = "taskDefinition.url")
	@Mapping(target = "scrapingConfiguration", source = "jobDefinition.scrapingDefinition")
	@Mapping(target = "storageConfiguration", source = "jobDefinition.storageDefinition")
	@Mapping(target = "id", ignore = true)
	Task map(JobDefinition jobDefinition, TaskDefinition taskDefinition);
}
