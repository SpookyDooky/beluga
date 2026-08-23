package com.beluga.mapper.job.scraping_configuration.extraction_definition.text;

import com.beluga.api.job.dto.read.extraction_configuration.text.ReadTextExtractionConfigurationDto;
import com.beluga.api.job.dto.write.extraction_configuration.text.WriteTextExtractionConfigurationDto;
import com.beluga.model.job_definition.configuration.scraping_configuration.extraction_definition.text.TextExtractionDefinition;
import com.beluga.properties.scraping.extraction_configuration.ExtractionConfigurationProperties;
import org.mapstruct.Mapper;

import static org.mapstruct.MappingConstants.ComponentModel.SPRING;

@Mapper(componentModel = SPRING)
public interface TextExtractionDefinitionMapper {

    TextExtractionDefinition map(WriteTextExtractionConfigurationDto dto);

    TextExtractionDefinition map(ExtractionConfigurationProperties properties);

    ReadTextExtractionConfigurationDto map(TextExtractionDefinition extractionDefinition);
}
