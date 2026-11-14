package com.beluga.mapper.job.scraping_configuration.extraction_definition.html;

import com.beluga.api.job.dto.read.extraction_configuration.html.ReadHtmlExtractionConfigurationDto;
import com.beluga.api.job.dto.write.extraction_configuration.html.WriteHtmlExtractionConfigurationDto;
import com.beluga.model.job_definition.configuration.scraping_configuration.extraction_definition.html.HtmlExtractionDefinition;
import com.beluga.properties.scraping.extraction_configuration.HtmlExtractionConfigurationProperties;
import org.mapstruct.Mapper;

import static org.mapstruct.MappingConstants.ComponentModel.SPRING;

@Mapper(componentModel = SPRING)
public interface HtmlExtractionDefinitionMapper {

    HtmlExtractionDefinition map(WriteHtmlExtractionConfigurationDto dto);

    HtmlExtractionDefinition map(HtmlExtractionConfigurationProperties properties);

    ReadHtmlExtractionConfigurationDto map(HtmlExtractionDefinition extractionDefinition);
}
