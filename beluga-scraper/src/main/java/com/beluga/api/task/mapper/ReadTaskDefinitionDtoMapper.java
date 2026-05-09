package com.beluga.api.task.mapper;

import com.beluga.api.task.dto.ReadTaskDefinitionDto;
import com.beluga.execution.model.task.TaskDefinition;
import org.mapstruct.Mapper;

import static org.mapstruct.MappingConstants.ComponentModel.SPRING;

@Mapper(componentModel = SPRING)
public interface ReadTaskDefinitionDtoMapper {

	ReadTaskDefinitionDto map(TaskDefinition taskDefinition);
}
