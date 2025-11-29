package com.x.scrape.api.task.mapper;

import com.x.scrape.api.task.dto.ReadTaskDefinitionDto;
import com.x.scrape.model.task.TaskDefinition;
import org.mapstruct.Mapper;

@Mapper
public interface ReadTaskDefinitionDtoMapper {

	ReadTaskDefinitionDto map(TaskDefinition taskDefinition);
}
