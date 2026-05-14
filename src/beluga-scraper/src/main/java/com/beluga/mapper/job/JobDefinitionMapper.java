package com.beluga.mapper.job;

import com.beluga.api.job.dto.read.ReadJobDefinitionDto;
import com.beluga.api.job.dto.write.WriteJobDefinitionDto;
import com.beluga.mapper.job.execution.ExecutionDefinitionMapper;
import com.beluga.mapper.job.scraping_configuration.ScrapingDefinitionMapper;
import com.beluga.mapper.job.storage.StorageDefinitionMapper;
import com.beluga.mapper.task.TaskDefinitionMapperService;
import com.beluga.model.job_definition.JobDefinition;
import com.beluga.properties.scraping.JobProperties;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import static org.mapstruct.InjectionStrategy.CONSTRUCTOR;
import static org.mapstruct.MappingConstants.ComponentModel.SPRING;

@Mapper(
		componentModel = SPRING,
		injectionStrategy = CONSTRUCTOR,
		uses = {
				ScrapingDefinitionMapper.class,
				StorageDefinitionMapper.class,
				ExecutionDefinitionMapper.class,
				TaskDefinitionMapperService.class
		}
)
public interface JobDefinitionMapper {
	
	@Mapping(target = "scrapingDefinition", source = "scraping")
	@Mapping(target = "storageDefinition", source = "storage")
	@Mapping(target = "executionDefinition", source = "execution")
	@Mapping(target = "taskDefinitions", source = "url")
	JobDefinition map(JobProperties jobProperties);
	
	@Mapping(target = "scrapingDefinition", source = "scraping")
	@Mapping(target = "storageDefinition", source = "storage")
	@Mapping(target = "executionDefinition", source = "execution")
	JobDefinition map(WriteJobDefinitionDto dto);
	
	@Mapping(target = "scraping", source = "scrapingDefinition")
	@Mapping(target = "storage", source = "storageDefinition")
	@Mapping(target = "execution", source = "executionDefinition")
	ReadJobDefinitionDto map(JobDefinition jobDefinition);
	
	@Mapping(target = "scrapingDefinition", source = "scraping")
	@Mapping(target = "storageDefinition", source = "storage")
	@Mapping(target = "executionDefinition", source = "execution")
	void update(WriteJobDefinitionDto dto, @MappingTarget JobDefinition jobDefinition);
}
