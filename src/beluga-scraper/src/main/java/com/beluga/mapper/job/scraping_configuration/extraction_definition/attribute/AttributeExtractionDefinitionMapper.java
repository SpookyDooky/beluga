package com.beluga.mapper.job.scraping_configuration.extraction_definition.attribute;

import com.beluga.api.job.dto.read.extraction_configuration.attribute.ReadAttributeExtractionConfigurationDto;
import com.beluga.api.job.dto.write.extraction_configuration.attribute.WriteAttributeExtractionConfigurationDto;
import com.beluga.model.job_definition.configuration.scraping_configuration.extraction_definition.attribute.AttributeExtractionDefinition;
import org.mapstruct.Mapper;

import static org.mapstruct.MappingConstants.ComponentModel.SPRING;

@Mapper(componentModel = SPRING)
public interface AttributeExtractionDefinitionMapper {

    AttributeExtractionDefinition map(WriteAttributeExtractionConfigurationDto dto);

    ReadAttributeExtractionConfigurationDto map(AttributeExtractionDefinition extractionDefinition);
}
