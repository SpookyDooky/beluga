package com.beluga.api.execution.mapper;

import com.beluga.api.execution.dto.ReadJobExecutionDto;
import com.beluga.api.execution.dto.ReadJobExecutionWithTasksDto;
import com.beluga.model.job_definition.JobExecution;
import org.mapstruct.Mapper;

import static org.mapstruct.InjectionStrategy.CONSTRUCTOR;
import static org.mapstruct.MappingConstants.ComponentModel.SPRING;

@Mapper(
		componentModel = SPRING,
		injectionStrategy = CONSTRUCTOR,
		uses = {
				ReadTaskExecutionMapper.class
		}
)
public interface ReadJobExecutionMapper {
	
	ReadJobExecutionDto map(JobExecution jobExecution);
	
	ReadJobExecutionWithTasksDto mapWithTasks(JobExecution jobExecution);
}
