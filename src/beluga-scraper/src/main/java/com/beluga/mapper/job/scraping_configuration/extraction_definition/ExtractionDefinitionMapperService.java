package com.beluga.mapper.job.scraping_configuration.extraction_definition;

import com.beluga.mapper.job.scraping_configuration.extraction_definition.attribute.AttributeExtractionDefinitionMapper;
import com.beluga.mapper.job.scraping_configuration.extraction_definition.description_list.DescriptionListExtractionDefinitionMapper;
import com.beluga.mapper.job.scraping_configuration.extraction_definition.html.HtmlExtractionDefinitionMapper;
import com.beluga.mapper.job.scraping_configuration.extraction_definition.image.ImageExtractionDefinitionMapper;
import com.beluga.mapper.job.scraping_configuration.extraction_definition.text.TextExtractionDefinitionMapper;
import com.beluga.model.job_definition.configuration.scraping_configuration.extraction_definition.ExtractionDefinition;
import com.beluga.properties.scraping.extraction_configuration.AttributeExtractionConfigurationProperties;
import com.beluga.properties.scraping.extraction_configuration.DescriptionListExtractionConfigurationProperties;
import com.beluga.properties.scraping.extraction_configuration.ExtractionConfigurationProperties;
import com.beluga.properties.scraping.extraction_configuration.HtmlExtractionConfigurationProperties;
import com.beluga.properties.scraping.extraction_configuration.ImageExtractionConfigurationProperties;
import com.beluga.properties.scraping.extraction_configuration.TextExtractionConfigurationProperties;
import org.springframework.stereotype.Service;

@Service
public class ExtractionDefinitionMapperService {

    private final AttributeExtractionDefinitionMapper attributeExtractionDefinitionMapper;
    private final DescriptionListExtractionDefinitionMapper descriptionListExtractionDefinitionMapper;
    private final HtmlExtractionDefinitionMapper htmlExtractionDefinitionMapper;
    private final ImageExtractionDefinitionMapper imageExtractionDefinitionMapper;
    private final TextExtractionDefinitionMapper textExtractionDefinitionMapper;

    public ExtractionDefinitionMapperService(final AttributeExtractionDefinitionMapper attributeExtractionDefinitionMapper,
                                             final DescriptionListExtractionDefinitionMapper descriptionListExtractionDefinitionMapper,
                                             final HtmlExtractionDefinitionMapper htmlExtractionDefinitionMapper,
                                             final ImageExtractionDefinitionMapper imageExtractionDefinitionMapper,
                                             final TextExtractionDefinitionMapper textExtractionDefinitionMapper) {
        this.attributeExtractionDefinitionMapper = attributeExtractionDefinitionMapper;
        this.descriptionListExtractionDefinitionMapper = descriptionListExtractionDefinitionMapper;
        this.htmlExtractionDefinitionMapper = htmlExtractionDefinitionMapper;
        this.imageExtractionDefinitionMapper = imageExtractionDefinitionMapper;
        this.textExtractionDefinitionMapper = textExtractionDefinitionMapper;
    }

    public ExtractionDefinition map(final ExtractionConfigurationProperties properties) {
        return switch (properties.configuredExtractionType()) {
            case AttributeExtractionConfigurationProperties attribute -> attributeExtractionDefinitionMapper.map(attribute);
            case DescriptionListExtractionConfigurationProperties descriptionList ->
                    descriptionListExtractionDefinitionMapper.map(descriptionList);
            case HtmlExtractionConfigurationProperties html -> htmlExtractionDefinitionMapper.map(html);
            case ImageExtractionConfigurationProperties image -> imageExtractionDefinitionMapper.map(image);
            case TextExtractionConfigurationProperties text -> textExtractionDefinitionMapper.map(text);
        };
    }
}
