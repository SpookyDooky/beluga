package com.x.scrape.api.execution.mapper;

import com.x.scrape.api.execution.dto.ReadJobExecutionDto;
import com.x.scrape.model.job_definition.JobExecution;
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
}
