package com.x.scrape.mapper.job;

import com.x.scrape.model.job.configuration.UrlConfiguration;
import com.x.scrape.properties.scraping.url.UrlProperties;
import org.mapstruct.Mapper;

import static org.mapstruct.MappingConstants.ComponentModel.SPRING;

@Mapper(componentModel = SPRING)
public interface UrlConfigurationMapper {
	
	UrlConfiguration map(UrlProperties urlProperties);
}
