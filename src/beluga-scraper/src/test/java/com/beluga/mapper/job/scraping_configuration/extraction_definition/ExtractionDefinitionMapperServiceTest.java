package com.beluga.mapper.job.scraping_configuration.extraction_definition;

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
import com.beluga.properties.scraping.extraction_configuration.ExtractionConfigurationProperties;
import org.instancio.Instancio;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

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
        final ExtractionConfigurationProperties properties = Instancio.of(ExtractionConfigurationProperties.class)
                .ignore(field(ExtractionConfigurationProperties::getDescriptionList))
                .ignore(field(ExtractionConfigurationProperties::getHtml))
                .ignore(field(ExtractionConfigurationProperties::getImage))
                .ignore(field(ExtractionConfigurationProperties::getText))
                .create();
        final AttributeExtractionDefinition extractionDefinition = mock();
        when(attributeExtractionDefinitionMapper.map(properties.getAttribute())).thenReturn(extractionDefinition);

        final ExtractionDefinition result = extractionDefinitionMapperService.map(properties);

        assertSame(extractionDefinition, result);
    }

    @Test
    void shouldMapToDescriptionListExtractionDefinition() {
        final ExtractionConfigurationProperties properties = Instancio.of(ExtractionConfigurationProperties.class)
                .ignore(field(ExtractionConfigurationProperties::getAttribute))
                .ignore(field(ExtractionConfigurationProperties::getHtml))
                .ignore(field(ExtractionConfigurationProperties::getImage))
                .ignore(field(ExtractionConfigurationProperties::getText))
                .create();
        final DescriptionListExtractionDefinition extractionDefinition = mock();
        when(descriptionListExtractionDefinitionMapper.map(properties.getDescriptionList())).thenReturn(extractionDefinition);

        final ExtractionDefinition result = extractionDefinitionMapperService.map(properties);

        assertSame(extractionDefinition, result);
    }

    @Test
    void shouldMapToHtmlExtractionDefinition() {
        final ExtractionConfigurationProperties properties = Instancio.of(ExtractionConfigurationProperties.class)
                .ignore(field(ExtractionConfigurationProperties::getAttribute))
                .ignore(field(ExtractionConfigurationProperties::getDescriptionList))
                .ignore(field(ExtractionConfigurationProperties::getImage))
                .ignore(field(ExtractionConfigurationProperties::getText))
                .create();
        final HtmlExtractionDefinition extractionDefinition = mock();
        when(htmlExtractionDefinitionMapper.map(properties.getHtml())).thenReturn(extractionDefinition);

        final ExtractionDefinition result = extractionDefinitionMapperService.map(properties);

        assertSame(extractionDefinition, result);
    }

    @Test
    void shouldMapToImageExtractionDefinition() {
        final ExtractionConfigurationProperties properties = Instancio.of(ExtractionConfigurationProperties.class)
                .ignore(field(ExtractionConfigurationProperties::getAttribute))
                .ignore(field(ExtractionConfigurationProperties::getDescriptionList))
                .ignore(field(ExtractionConfigurationProperties::getHtml))
                .ignore(field(ExtractionConfigurationProperties::getText))
                .create();
        final ImageExtractionDefinition extractionDefinition = mock();
        when(imageExtractionDefinitionMapper.map(properties.getImage())).thenReturn(extractionDefinition);

        final ExtractionDefinition result = extractionDefinitionMapperService.map(properties);

        assertSame(extractionDefinition, result);
    }

    @Test
    void shouldMapToTextExtractionDefinition() {
        final ExtractionConfigurationProperties properties = Instancio.of(ExtractionConfigurationProperties.class)
                .ignore(field(ExtractionConfigurationProperties::getAttribute))
                .ignore(field(ExtractionConfigurationProperties::getDescriptionList))
                .ignore(field(ExtractionConfigurationProperties::getHtml))
                .ignore(field(ExtractionConfigurationProperties::getImage))
                .create();
        final TextExtractionDefinition extractionDefinition = mock();
        when(textExtractionDefinitionMapper.map(properties.getText())).thenReturn(extractionDefinition);

        final ExtractionDefinition result = extractionDefinitionMapperService.map(properties);

        assertSame(extractionDefinition, result);
    }
}
