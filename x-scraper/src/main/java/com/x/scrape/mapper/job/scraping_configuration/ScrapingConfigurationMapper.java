package com.x.scrape.mapper.job.scraping_configuration;

import com.x.scrape.model.job_definition.configuration.scraping_configuration.ScrapingConfiguration;
import com.x.scrape.properties.scraping.ScrapingProperties;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import static org.mapstruct.InjectionStrategy.CONSTRUCTOR;
import static org.mapstruct.MappingConstants.ComponentModel.SPRING;

@Mapper(
		componentModel = SPRING,
		injectionStrategy = CONSTRUCTOR,
		uses = {
				DataPointConfigurationMapper.class
		}
)
public interface ScrapingConfigurationMapper {
	
	@Mapping(target = "dataPointConfigurations", source = "dataPoints")
	ScrapingConfiguration map(ScrapingProperties scrapingProperties);
}
