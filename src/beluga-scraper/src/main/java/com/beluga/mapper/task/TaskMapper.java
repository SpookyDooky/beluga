package com.beluga.mapper.task;

import com.beluga.mapper.task.configuration.ScrapingConfigurationMapper;
import com.beluga.model.job_definition.JobDefinition;
import com.beluga.execution.model.task.Task;
import com.beluga.execution.model.task.TaskDefinition;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import static org.mapstruct.InjectionStrategy.CONSTRUCTOR;
import static org.mapstruct.MappingConstants.ComponentModel.SPRING;

@Mapper(
		componentModel = SPRING,
		injectionStrategy = CONSTRUCTOR,
		uses = {
				ScrapingConfigurationMapper.class
		}
)
public interface TaskMapper {

	@Mapping(target = "url", source = "taskDefinition.url")
	@Mapping(target = "scrapingConfiguration", source = "jobDefinition.scrapingDefinition")
	@Mapping(target = "id", ignore = true)
	Task map(JobDefinition jobDefinition, TaskDefinition taskDefinition);
}
