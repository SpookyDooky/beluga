package com.x.scrape.mapper.job.scraping_configuration;

import com.x.scrape.api.job.dto.read.ReadScrapingConfigurationDto;
import com.x.scrape.api.job.dto.write.WriteScrapingConfigurationDto;
import com.x.scrape.model.job_definition.configuration.scraping_configuration.ScrapingDefinition;
import com.x.scrape.properties.scraping.ScrapingProperties;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import static org.mapstruct.CollectionMappingStrategy.TARGET_IMMUTABLE;
import static org.mapstruct.InjectionStrategy.CONSTRUCTOR;
import static org.mapstruct.MappingConstants.ComponentModel.SPRING;

@Mapper(
		componentModel = SPRING,
		injectionStrategy = CONSTRUCTOR,
		collectionMappingStrategy = TARGET_IMMUTABLE,
		uses = {
				DataPointConfigurationMapper.class
		}
)
public interface ScrapingConfigurationMapper {
	
	@Mapping(target = "dataPointDefinitions", source = "dataPoints")
	ScrapingDefinition map(ScrapingProperties scrapingProperties);
	
	@Mapping(target = "dataPointDefinitions", source = "dataPoints")
	ScrapingDefinition map(WriteScrapingConfigurationDto dto);
	
	@Mapping(target = "dataPoints", source = "dataPointDefinitions")
	ReadScrapingConfigurationDto map(ScrapingDefinition scrapingDefinition);
	
	@Mapping(target = "dataPointDefinitions", source = "dataPoints")
	void update(WriteScrapingConfigurationDto dto, @MappingTarget ScrapingDefinition scrapingDefinition);
}
