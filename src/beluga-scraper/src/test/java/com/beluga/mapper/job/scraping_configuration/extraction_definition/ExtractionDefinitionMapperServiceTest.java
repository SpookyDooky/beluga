package com.beluga.mapper.job.scraping_configuration.extraction_definition;

import com.beluga.mapper.job.scraping_configuration.extraction_definition.attribute.AttributeExtractionDefinitionMapper;
import com.beluga.mapper.job.scraping_configuration.extraction_definition.description_list.DescriptionListExtractionDefinitionMapper;
import com.beluga.mapper.job.scraping_configuration.extraction_definition.html.HtmlExtractionDefinitionMapper;
import com.beluga.mapper.job.scraping_configuration.extraction_definition.image.ImageExtractionDefinitionMapper;
import com.beluga.mapper.job.scraping_configuration.extraction_definition.text.TextExtractionDefinitionMapper;
import com.beluga.model.job_definition.configuration.scraping_configuration.ExtractionType;
import com.beluga.model.job_definition.configuration.scraping_configuration.extraction_definition.ExtractionDefinition;
import com.beluga.model.job_definition.configuration.scraping_configuration.extraction_definition.attribute.AttributeExtractionDefinition;
import com.beluga.model.job_definition.configuration.scraping_configuration.extraction_definition.description_list.DescriptionListExtractionDefinition;
import com.beluga.model.job_definition.configuration.scraping_configuration.extraction_definition.html.HtmlExtractionDefinition;
import com.beluga.model.job_definition.configuration.scraping_configuration.extraction_definition.image.ImageExtractionDefinition;
import com.beluga.model.job_definition.configuration.scraping_configuration.extraction_definition.text.TextExtractionDefinition;
import com.beluga.properties.scraping.extraction_configuration.ExtractionConfigurationProperties;
import org.instancio.Instancio;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static com.beluga.model.job_definition.configuration.scraping_configuration.ExtractionType.*;
import static org.instancio.Select.field;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ExtractionDefinitionMapperServiceTest {

    @Mock
    private AttributeExtractionDefinitionMapper attributeExtractionDefinitionMapper;
    @Mock
    private DescriptionListExtractionDefinitionMapper descriptionListExtractionDefinitionMapper;
    @Mock
    private HtmlExtractionDefinitionMapper htmlExtractionDefinitionMapper;
    @Mock
    private ImageExtractionDefinitionMapper imageExtractionDefinitionMapper;
    @Mock
    private TextExtractionDefinitionMapper textExtractionDefinitionMapper;

    @InjectMocks
    private ExtractionDefinitionMapperService extractionDefinitionMapperService;

    @Test
    void shouldMapToAttributeExtractionDefinition() {
        final ExtractionConfigurationProperties properties = createExtractionConfigurationProperties(ATTRIBUTE);
        final AttributeExtractionDefinition extractionDefinition = mock();
        when(attributeExtractionDefinitionMapper.map(properties)).thenReturn(extractionDefinition);

        final ExtractionDefinition result = extractionDefinitionMapperService.map(properties);

        assertSame(extractionDefinition, result);
    }

    ExtractionConfigurationProperties createExtractionConfigurationProperties(final ExtractionType type) {
        return Instancio.of(ExtractionConfigurationProperties.class)
                .set(field(ExtractionConfigurationProperties::getType), type)
                .create();
    }

    @Test
    void shouldMapToDescriptionListExtractionDefinition() {
        final ExtractionConfigurationProperties properties = createExtractionConfigurationProperties(DESCRIPTION_LIST);
        final DescriptionListExtractionDefinition extractionDefinition = mock();
        when(descriptionListExtractionDefinitionMapper.map(properties)).thenReturn(extractionDefinition);

        final ExtractionDefinition result = extractionDefinitionMapperService.map(properties);

        assertSame(extractionDefinition, result);
    }

    @Test
    void shouldMapToHtmlExtractionDefinition() {
        final ExtractionConfigurationProperties properties = createExtractionConfigurationProperties(HTML);
        final HtmlExtractionDefinition extractionDefinition = mock();
        when(htmlExtractionDefinitionMapper.map(properties)).thenReturn(extractionDefinition);

        final ExtractionDefinition result = extractionDefinitionMapperService.map(properties);

        assertSame(extractionDefinition, result);
    }

    @Test
    void shouldMapToImageExtractionDefinition() {
        final ExtractionConfigurationProperties properties = createExtractionConfigurationProperties(IMAGE);
        final ImageExtractionDefinition extractionDefinition = mock();
        when(imageExtractionDefinitionMapper.map(properties)).thenReturn(extractionDefinition);

        final ExtractionDefinition result = extractionDefinitionMapperService.map(properties);

        assertSame(extractionDefinition, result);
    }

    @Test
    void shouldMapToTextExtractionDefinition() {
        final ExtractionConfigurationProperties properties = createExtractionConfigurationProperties(TEXT);
        final TextExtractionDefinition extractionDefinition = mock();
        when(textExtractionDefinitionMapper.map(properties)).thenReturn(extractionDefinition);

        final ExtractionDefinition result = extractionDefinitionMapperService.map(properties);

        assertSame(extractionDefinition, result);
    }
}