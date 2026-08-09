package com.beluga.mapper.task.configuration.extraction_configuration;

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
import com.beluga.model.job_definition.configuration.scraping_configuration.extraction_definition.attribute.AttributeExtractionDefinition;
import com.beluga.model.job_definition.configuration.scraping_configuration.extraction_definition.description_list.DescriptionListExtractionDefinition;
import com.beluga.model.job_definition.configuration.scraping_configuration.extraction_definition.html.HtmlExtractionDefinition;
import com.beluga.model.job_definition.configuration.scraping_configuration.extraction_definition.image.ImageExtractionDefinition;
import com.beluga.model.job_definition.configuration.scraping_configuration.extraction_definition.text.TextExtractionDefinition;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ExtractionConfigurationMapperTest {

    @Mock
    private AttributeExtractionConfigurationMapper attributeExtractionConfigurationMapper;
    @Mock
    private DescriptionListExtractionConfigurationMapper descriptionListExtractionConfigurationMapper;
    @Mock
    private HtmlExtractionConfigurationMapper htmlExtractionConfigurationMapper;
    @Mock
    private ImageExtractionConfigurationMapper imageExtractionConfigurationMapper;
    @Mock
    private TextExtractionConfigurationMapper textExtractionConfigurationMapper;

    @InjectMocks
    private ExtractionConfigurationMapperImpl mapper;

    @Test
    void shouldMapToAttributeExtractionConfiguration() {
        final AttributeExtractionDefinition definition = mock();
        final AttributeExtractionConfiguration configuration = mock();
        when(attributeExtractionConfigurationMapper.map(definition)).thenReturn(configuration);

        assertInstanceOf(AttributeExtractionConfiguration.class, mapper.map(definition));
    }

    @Test
    void shouldMapToDescriptionListExtractionConfiguration() {
        final DescriptionListExtractionDefinition definition = mock();
        final DescriptionListExtractionConfiguration configuration = mock();
        when(descriptionListExtractionConfigurationMapper.map(definition)).thenReturn(configuration);

        assertInstanceOf(AttributeExtractionConfiguration.class, mapper.map(definition));
    }

    @Test
    void shouldMapToHtmlExtractionConfiguration() {
        final HtmlExtractionDefinition definition = mock();
        final HtmlExtractionConfiguration configuration = mock();
        when(htmlExtractionConfigurationMapper.map(definition)).thenReturn(configuration);

        assertInstanceOf(AttributeExtractionConfiguration.class, mapper.map(definition));
    }

    @Test
    void shouldMapToImageExtractionConfigurationMapper() {
        final ImageExtractionDefinition definition = mock();
        final ImageExtractionConfiguration configuration = mock();
        when(imageExtractionConfigurationMapper.map(definition)).thenReturn(configuration);

        assertInstanceOf(AttributeExtractionConfiguration.class, mapper.map(definition));
    }

    @Test
    void shouldMapToTextExtractionConfigurationMapper() {
        final TextExtractionDefinition definition = mock();
        final TextExtractionConfiguration configuration = mock();
        when(textExtractionConfigurationMapper.map(definition)).thenReturn(configuration);

        assertInstanceOf(AttributeExtractionConfiguration.class, mapper.map(definition));
    }
}