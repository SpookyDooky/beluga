package com.beluga.mapper.job.scraping_configuration;

import com.beluga.api.job.dto.read.ReadScrapingConfigurationDto;
import com.beluga.api.job.dto.write.WriteScrapingConfigurationDto;
import com.beluga.mapper.job.scraping_configuration.extraction_definition.ExtractionDefinitionMapper;
import com.beluga.model.job_definition.configuration.scraping_configuration.ScrapingDefinition;
import com.beluga.properties.scraping.ScrapingProperties;
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
				ExtractionDefinitionMapper.class
		}
)
public interface ScrapingDefinitionMapper {
	
//	@Mapping(target = "dataPointDefinitions", source = "dataPoints")
	ScrapingDefinition map(ScrapingProperties scrapingProperties);
	
	@Mapping(target = "extractionDefinitions", source = "dataPoints")
	ScrapingDefinition map(WriteScrapingConfigurationDto dto);
	
	@Mapping(target = "dataPoints", source = "extractionDefinitions")
	ReadScrapingConfigurationDto map(ScrapingDefinition scrapingDefinition);
	
	@Mapping(target = "extractionDefinitions", source = "dataPoints")
	void update(WriteScrapingConfigurationDto dto, @MappingTarget ScrapingDefinition scrapingDefinition);
}
