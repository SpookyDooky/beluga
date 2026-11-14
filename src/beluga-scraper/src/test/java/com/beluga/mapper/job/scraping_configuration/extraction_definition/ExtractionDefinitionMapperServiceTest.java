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
import com.beluga.properties.scraping.extraction_configuration.AttributeExtractionConfigurationProperties;
import com.beluga.properties.scraping.extraction_configuration.DescriptionListExtractionConfigurationProperties;
import com.beluga.properties.scraping.extraction_configuration.ExtractionConfigurationProperties;
import com.beluga.properties.scraping.extraction_configuration.HtmlExtractionConfigurationProperties;
import com.beluga.properties.scraping.extraction_configuration.ImageExtractionConfigurationProperties;
import com.beluga.properties.scraping.extraction_configuration.TextExtractionConfigurationProperties;
import org.instancio.Instancio;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

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
        final AttributeExtractionConfigurationProperties attribute = Instancio.create(AttributeExtractionConfigurationProperties.class);
        final ExtractionConfigurationProperties properties = new ExtractionConfigurationProperties();
        properties.setAttribute(attribute);
        final AttributeExtractionDefinition extractionDefinition = mock();
        when(attributeExtractionDefinitionMapper.map(attribute)).thenReturn(extractionDefinition);

        final ExtractionDefinition result = extractionDefinitionMapperService.map(properties);

        assertSame(extractionDefinition, result);
    }

    @Test
    void shouldMapToDescriptionListExtractionDefinition() {
        final DescriptionListExtractionConfigurationProperties descriptionList =
                Instancio.create(DescriptionListExtractionConfigurationProperties.class);
        final ExtractionConfigurationProperties properties = new ExtractionConfigurationProperties();
        properties.setDescriptionList(descriptionList);
        final DescriptionListExtractionDefinition extractionDefinition = mock();
        when(descriptionListExtractionDefinitionMapper.map(descriptionList)).thenReturn(extractionDefinition);

        final ExtractionDefinition result = extractionDefinitionMapperService.map(properties);

        assertSame(extractionDefinition, result);
    }

    @Test
    void shouldMapToHtmlExtractionDefinition() {
        final HtmlExtractionConfigurationProperties html = Instancio.create(HtmlExtractionConfigurationProperties.class);
        final ExtractionConfigurationProperties properties = new ExtractionConfigurationProperties();
        properties.setHtml(html);
        final HtmlExtractionDefinition extractionDefinition = mock();
        when(htmlExtractionDefinitionMapper.map(html)).thenReturn(extractionDefinition);

        final ExtractionDefinition result = extractionDefinitionMapperService.map(properties);

        assertSame(extractionDefinition, result);
    }

    @Test
    void shouldMapToImageExtractionDefinition() {
        final ImageExtractionConfigurationProperties image = new ImageExtractionConfigurationProperties();
        final ExtractionConfigurationProperties properties = new ExtractionConfigurationProperties();
        properties.setImage(image);
        final ImageExtractionDefinition extractionDefinition = mock();
        when(imageExtractionDefinitionMapper.map(image)).thenReturn(extractionDefinition);

        final ExtractionDefinition result = extractionDefinitionMapperService.map(properties);

        assertSame(extractionDefinition, result);
    }

    @Test
    void shouldMapToTextExtractionDefinition() {
        final TextExtractionConfigurationProperties text = Instancio.create(TextExtractionConfigurationProperties.class);
        final ExtractionConfigurationProperties properties = new ExtractionConfigurationProperties();
        properties.setText(text);
        final TextExtractionDefinition extractionDefinition = mock();
        when(textExtractionDefinitionMapper.map(text)).thenReturn(extractionDefinition);

        final ExtractionDefinition result = extractionDefinitionMapperService.map(properties);

        assertSame(extractionDefinition, result);
    }
}
