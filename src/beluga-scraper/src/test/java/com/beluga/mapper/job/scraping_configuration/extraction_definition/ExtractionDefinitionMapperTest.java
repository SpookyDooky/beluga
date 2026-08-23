package com.beluga.mapper.job.scraping_configuration.extraction_definition;

import com.beluga.api.job.dto.read.extraction_configuration.attribute.ReadAttributeExtractionConfigurationDto;
import com.beluga.api.job.dto.read.extraction_configuration.description_list.ReadDescriptionListExtractionConfigurationDto;
import com.beluga.api.job.dto.read.extraction_configuration.html.ReadHtmlExtractionConfigurationDto;
import com.beluga.api.job.dto.read.extraction_configuration.image.ReadImageExtractionConfigurationDto;
import com.beluga.api.job.dto.read.extraction_configuration.text.ReadTextExtractionConfigurationDto;
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

    @Test
    void shouldMapReadAttributeConfigurationDtoFromExtractionDefinition() {
        final AttributeExtractionDefinition definition = mock();
        final ReadAttributeExtractionConfigurationDto expectedDto = mock();
        when(attributeExtractionDefinitionMapper.map(definition)).thenReturn(expectedDto);

        final ReadAttributeExtractionConfigurationDto result = assertInstanceOf(
                ReadAttributeExtractionConfigurationDto.class,
                mapper.map(definition)
        );

        assertSame(expectedDto, result);
    }

    @Test
    void shouldMapReadDescriptionListConfigurationDtoFromExtractionDefinition() {
        final DescriptionListExtractionDefinition definition = mock();
        final ReadDescriptionListExtractionConfigurationDto expectedDto = mock();
        when(descriptionListExtractionDefinitionMapper.map(definition)).thenReturn(expectedDto);

        final ReadDescriptionListExtractionConfigurationDto result = assertInstanceOf(
                ReadDescriptionListExtractionConfigurationDto.class,
                mapper.map(definition)
        );

        assertSame(expectedDto, result);
    }

    @Test
    void shouldMapReadHtmlConfigurationDtoFromExtractionDefinition() {
        final HtmlExtractionDefinition definition = mock();
        final ReadHtmlExtractionConfigurationDto expectedDto = mock();
        when(htmlExtractionDefinitionMapper.map(definition)).thenReturn(expectedDto);

        final ReadHtmlExtractionConfigurationDto result = assertInstanceOf(
                ReadHtmlExtractionConfigurationDto.class,
                mapper.map(definition)
        );

        assertSame(expectedDto, result);
    }

    @Test
    void shouldMapReadImageConfigurationDtoFromExtractionDefinition() {
        final ImageExtractionDefinition definition = mock();
        final ReadImageExtractionConfigurationDto expectedDto = mock();
        when(imageExtractionDefinitionMapper.map(definition)).thenReturn(expectedDto);

        final ReadImageExtractionConfigurationDto result = assertInstanceOf(
                ReadImageExtractionConfigurationDto.class,
                mapper.map(definition)
        );

        assertSame(expectedDto, result);
    }

    @Test
    void shouldMapReadTextConfigurationDtoFromExtractionDefinition() {
        final TextExtractionDefinition definition = mock();
        final ReadTextExtractionConfigurationDto expectedDto = mock();
        when(textExtractionDefinitionMapper.map(definition)).thenReturn(expectedDto);

        final ReadTextExtractionConfigurationDto result = assertInstanceOf(
                ReadTextExtractionConfigurationDto.class,
                mapper.map(definition)
        );

        assertSame(expectedDto, result);
    }
}