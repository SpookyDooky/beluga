package com.beluga.mapper.job.scraping_configuration.extraction_definition;

import com.beluga.api.job.dto.write.extraction_configuration.WriteExtractionConfigurationDto;
import com.beluga.api.job.dto.write.extraction_configuration.attribute.WriteAttributeExtractionConfigurationDto;
import com.beluga.api.job.dto.write.extraction_configuration.description_list.WriteDescriptionListExtractionConfigurationDto;
import com.beluga.api.job.dto.write.extraction_configuration.html.WriteHtmlExtractionConfigurationDto;
import com.beluga.api.job.dto.write.extraction_configuration.image.WriteImageExtractionConfigurationDto;
import com.beluga.api.job.dto.write.extraction_configuration.text.WriteTextExtractionConfigurationDto;
import com.beluga.mapper.job.scraping_configuration.extraction_definition.attribute.AttributeExtractionDefinitionMapper;
import com.beluga.mapper.job.scraping_configuration.extraction_definition.description_list.DescriptionListExtractionDefinitionMapper;
import com.beluga.mapper.job.scraping_configuration.extraction_definition.html.HtmlExtractionDefinitionMapper;
import com.beluga.mapper.job.scraping_configuration.extraction_definition.image.ImageExtractionDefinitionMapper;
import com.beluga.mapper.job.scraping_configuration.extraction_definition.text.TextExtractionDefinitionMapper;
import com.beluga.model.job_definition.configuration.scraping_configuration.extraction_definition.ExtractionDefinition;
import com.beluga.model.job_definition.configuration.scraping_configuration.extraction_definition.attribute.AttributeExtractionDefinition;
import com.beluga.model.job_definition.configuration.scraping_configuration.extraction_definition.description_list.DescriptionListExtractionDefinition;
import com.beluga.model.job_definition.configuration.scraping_configuration.extraction_definition.html.HtmlExtractionDefinition;
import com.beluga.model.job_definition.configuration.scraping_configuration.extraction_definition.image.ImageExtractionDefinition;
import com.beluga.model.job_definition.configuration.scraping_configuration.extraction_definition.text.TextExtractionDefinition;
import org.mapstruct.Mapper;
import org.mapstruct.SubclassMapping;

import static org.mapstruct.InjectionStrategy.CONSTRUCTOR;
import static org.mapstruct.MappingConstants.ComponentModel.SPRING;
import static org.mapstruct.SubclassExhaustiveStrategy.RUNTIME_EXCEPTION;

@Mapper(
        componentModel = SPRING,
        injectionStrategy = CONSTRUCTOR,
        subclassExhaustiveStrategy = RUNTIME_EXCEPTION,
        uses = {
                AttributeExtractionDefinitionMapper.class,
                DescriptionListExtractionDefinitionMapper.class,
                HtmlExtractionDefinitionMapper.class,
                ImageExtractionDefinitionMapper.class,
                TextExtractionDefinitionMapper.class
        }
)
public interface ExtractionDefinitionMapper {

    @SubclassMapping(target = AttributeExtractionDefinition.class, source = WriteAttributeExtractionConfigurationDto.class)
    @SubclassMapping(target = DescriptionListExtractionDefinition.class, source = WriteDescriptionListExtractionConfigurationDto.class)
    @SubclassMapping(target = HtmlExtractionDefinition.class, source = WriteHtmlExtractionConfigurationDto.class)
    @SubclassMapping(target = ImageExtractionDefinition.class, source = WriteImageExtractionConfigurationDto.class)
    @SubclassMapping(target = TextExtractionDefinition.class, source = WriteTextExtractionConfigurationDto.class)
    ExtractionDefinition map(WriteExtractionConfigurationDto dto);
}
