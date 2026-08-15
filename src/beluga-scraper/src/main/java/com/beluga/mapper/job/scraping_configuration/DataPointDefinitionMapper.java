package com.beluga.mapper.job.scraping_configuration;

import com.beluga.api.job.dto.read.ReadDataPointConfigurationDto;
import com.beluga.api.job.dto.write.WriteDataPointConfigurationDto;
import com.beluga.mapper.job.scraping_configuration.extraction_definition.ExtractionDefinitionMapper;
import com.beluga.model.job_definition.configuration.scraping_configuration.DataPointDefinition;
import com.beluga.properties.scraping.DataPointProperties;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import static org.mapstruct.MappingConstants.ComponentModel.SPRING;

@Mapper(
		componentModel = SPRING,
		uses = {
				ExtractionDefinitionMapper.class
		}
)
public interface DataPointDefinitionMapper {
	
	DataPointDefinition map(DataPointProperties dataPointProperties);

	@Mapping(target = "extractionDefinition", source = "extraction")
	DataPointDefinition map(WriteDataPointConfigurationDto writeDataPointConfigurationDto);

	@Mapping(target = "extraction", source = "extractionDefinition")
	ReadDataPointConfigurationDto map(DataPointDefinition dataPointDefinition);
}
