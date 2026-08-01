package com.beluga.mapper.job.scraping_configuration.extraction_definition.description_list;

import com.beluga.api.job.dto.write.extraction_configuration.description_list.WriteDescriptionListExtractionConfigurationDto;
import com.beluga.model.job_definition.configuration.scraping_configuration.extraction_definition.description_list.DescriptionListExtractionDefinition;
import org.mapstruct.Mapper;

import static org.mapstruct.InjectionStrategy.CONSTRUCTOR;
import static org.mapstruct.MappingConstants.ComponentModel.SPRING;

@Mapper(
        componentModel = SPRING,
        injectionStrategy = CONSTRUCTOR,
        uses = {
                DescriptionListDataPointDefinitionMapper.class
        }
)
public interface DescriptionListExtractionDefinitionMapper {

    DescriptionListExtractionDefinition map(WriteDescriptionListExtractionConfigurationDto dto);
}
