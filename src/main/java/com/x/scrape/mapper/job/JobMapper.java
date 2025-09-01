package com.x.scrape.mapper.job;

import com.x.scrape.mapper.job.scraping_configuration.ScrapingConfigurationMapper;
import com.x.scrape.mapper.job.storage.StorageConfigurationMapper;
import com.x.scrape.model.job.Job;
import com.x.scrape.properties.scraping.ScrapingProperties;
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
				StorageConfigurationMapper.class
		}
)
public interface JobMapper {
	
	@Mapping(target = "urlConfiguration", source = "url")
	@Mapping(target = "scrapingConfiguration", source = "dataScraping")
	@Mapping(target = "storageConfiguration", source = "storage")
	Job map(ScrapingProperties scrapingProperties);
}
