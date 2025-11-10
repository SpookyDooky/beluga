package com.x.scrape.mapper.job;

import com.x.scrape.mapper.job.execution.ExecutionConfigurationMapper;
import com.x.scrape.mapper.job.scraping_configuration.ScrapingConfigurationMapper;
import com.x.scrape.mapper.job.storage.StorageConfigurationMapper;
import com.x.scrape.mapper.task.TaskDefinitionMapperService;
import com.x.scrape.model.job_definition.JobDefinition;
import com.x.scrape.properties.scraping.JobProperties;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import static org.mapstruct.InjectionStrategy.CONSTRUCTOR;
import static org.mapstruct.MappingConstants.ComponentModel.SPRING;

@Mapper(
		componentModel = SPRING,
		injectionStrategy = CONSTRUCTOR,
		uses = {
				UrlConfigurationMapper.class,
				ScrapingConfigurationMapper.class,
				StorageConfigurationMapper.class,
				ExecutionConfigurationMapper.class,
				TaskDefinitionMapperService.class
		}
)
public interface JobDefinitionMapper {
	
	@Mapping(target = "urlConfiguration", source = "url")
	@Mapping(target = "scrapingConfiguration", source = "scraping")
	@Mapping(target = "storageConfiguration", source = "storage")
	@Mapping(target = "executionConfiguration", source = "execution")
	@Mapping(target = "taskDefinitions", source = "url")
	JobDefinition map(JobProperties jobProperties);
}
