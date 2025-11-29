package com.x.scrape.api.task.mapper;

import com.x.scrape.api.task.dto.ReadTaskDefinitionDto;
import com.x.scrape.model.task.TaskDefinition;
import org.mapstruct.Mapper;

import static org.mapstruct.MappingConstants.ComponentModel.SPRING;

@Mapper(componentModel = SPRING)
public interface ReadTaskDefinitionDtoMapper {

	ReadTaskDefinitionDto map(TaskDefinition taskDefinition);
}
