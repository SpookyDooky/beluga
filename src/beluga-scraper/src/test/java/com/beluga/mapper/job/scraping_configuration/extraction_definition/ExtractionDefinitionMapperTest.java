package com.beluga.mapper.job.scraping_configuration.extraction_definition;

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
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ExtractionDefinitionMapperTest {

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
    private ExtractionDefinitionMapperImpl mapper;

    @Test
    void shouldMapAttributeExtractionDefinitionFromWriteDto() {
        final WriteAttributeExtractionConfigurationDto dto = mock();
        final AttributeExtractionDefinition expectedDefinition = mock();
        when(attributeExtractionDefinitionMapper.map(dto)).thenReturn(expectedDefinition);

        final AttributeExtractionDefinition result = assertInstanceOf(
                AttributeExtractionDefinition.class,
                mapper.map(dto)
        );

        assertSame(expectedDefinition, result);
    }

    @Test
    void shouldMapDescriptionListExtractionDefinitionFromWriteDto() {
        final WriteDescriptionListExtractionConfigurationDto dto = mock();
        final DescriptionListExtractionDefinition expectedDefinition = mock();
        when(descriptionListExtractionDefinitionMapper.map(dto)).thenReturn(expectedDefinition);

        final DescriptionListExtractionDefinition result = assertInstanceOf(
                DescriptionListExtractionDefinition.class,
                mapper.map(dto)
        );

        assertSame(expectedDefinition, result);
    }

    @Test
    void shouldMapHtmlExtractionDefinitionFromWriteDto() {
        final WriteHtmlExtractionConfigurationDto dto = mock();
        final HtmlExtractionDefinition expectedDefinition = mock();
        when(htmlExtractionDefinitionMapper.map(dto)).thenReturn(expectedDefinition);

        final HtmlExtractionDefinition result = assertInstanceOf(
                HtmlExtractionDefinition.class,
                mapper.map(dto)
        );

        assertSame(expectedDefinition, result);
    }

    @Test
    void shouldMapImageExtractionDefinitionFromWriteDto() {
        final WriteImageExtractionConfigurationDto dto = mock();
        final ImageExtractionDefinition expectedDefinition = mock();
        when(imageExtractionDefinitionMapper.map(dto)).thenReturn(expectedDefinition);

        final ImageExtractionDefinition result = assertInstanceOf(
                ImageExtractionDefinition.class,
                mapper.map(dto)
        );

        assertSame(expectedDefinition, result);
    }

    @Test
    void shouldMapTextExtractionDefinitionFromWriteDto() {
        final WriteTextExtractionConfigurationDto dto = mock();
        final TextExtractionDefinition expectedDefinition = mock();
        when(textExtractionDefinitionMapper.map(dto)).thenReturn(expectedDefinition);

        final TextExtractionDefinition result = assertInstanceOf(
                TextExtractionDefinition.class,
                mapper.map(dto)
        );

        assertSame(expectedDefinition, result);
    }
}