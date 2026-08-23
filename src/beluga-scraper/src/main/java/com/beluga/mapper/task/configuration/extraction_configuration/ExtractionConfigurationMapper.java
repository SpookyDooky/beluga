package com.beluga.mapper.task.configuration.extraction_configuration;

import com.beluga.execution.model.task.extraction_configuration.ExtractionConfiguration;
import com.beluga.execution.model.task.extraction_configuration.attribute.AttributeExtractionConfiguration;
import com.beluga.execution.model.task.extraction_configuration.description_list.DescriptionListExtractionConfiguration;
import com.beluga.execution.model.task.extraction_configuration.html.HtmlExtractionConfiguration;
import com.beluga.execution.model.task.extraction_configuration.image.ImageExtractionConfiguration;
import com.beluga.execution.model.task.extraction_configuration.text.TextExtractionConfiguration;
import com.beluga.mapper.task.configuration.extraction_configuration.attribute.AttributeExtractionConfigurationMapper;
import com.beluga.mapper.task.configuration.extraction_configuration.description_list.DescriptionListExtractionConfigurationMapper;
import com.beluga.mapper.task.configuration.extraction_configuration.html.HtmlExtractionConfigurationMapper;
import com.beluga.mapper.task.configuration.extraction_configuration.image.ImageExtractionConfigurationMapper;
import com.beluga.mapper.task.configuration.extraction_configuration.text.TextExtractionConfigurationMapper;
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
                AttributeExtractionConfigurationMapper.class,
                DescriptionListExtractionConfigurationMapper.class,
                HtmlExtractionConfigurationMapper.class,
                ImageExtractionConfigurationMapper.class,
                TextExtractionConfigurationMapper.class
        }
)
public interface ExtractionConfigurationMapper {

    @SubclassMapping(target = AttributeExtractionConfiguration.class, source = AttributeExtractionDefinition.class)
    @SubclassMapping(target = DescriptionListExtractionConfiguration.class, source = DescriptionListExtractionDefinition.class)
    @SubclassMapping(target = HtmlExtractionConfiguration.class, source = HtmlExtractionDefinition.class)
    @SubclassMapping(target = ImageExtractionConfiguration.class, source = ImageExtractionDefinition.class)
    @SubclassMapping(target = TextExtractionConfiguration.class, source = TextExtractionDefinition.class)
    ExtractionConfiguration map(ExtractionDefinition definition);
}
