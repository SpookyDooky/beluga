package com.beluga.api.execution.mapper;

import com.beluga.api.execution.dto.ReadTaskExecutionDto;
import com.beluga.execution.model.task.TaskExecution;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import static org.mapstruct.MappingConstants.ComponentModel.SPRING;

@Mapper(componentModel = SPRING)
public interface ReadTaskExecutionMapper {
	
	@Mapping(target = "url", source = "taskDefinition.url")
	ReadTaskExecutionDto map(TaskExecution taskExecution);
}
