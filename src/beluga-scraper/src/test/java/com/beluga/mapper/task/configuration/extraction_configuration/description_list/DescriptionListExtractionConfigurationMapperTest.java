package com.beluga.mapper.task.configuration.extraction_configuration.description_list;

import com.beluga.execution.model.task.extraction_configuration.description_list.DescriptionListExtractionConfiguration;
import com.beluga.execution.model.task.extraction_configuration.description_list.DescriptionListExtractionDataPointConfiguration;
import com.beluga.model.job_definition.configuration.scraping_configuration.extraction_definition.description_list.DescriptionListExtractionDefinition;
import org.instancio.Instancio;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.instancio.settings.Keys.COLLECTION_MAX_SIZE;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DescriptionListExtractionConfigurationMapperTest {

    @Mock
    private DescriptionListExtractionDataPointConfigurationMapper descriptionListExtractionDataPointConfigurationMapper;
    @InjectMocks
    private DescriptionListExtractionConfigurationMapperImpl mapper;

    @Test
    void shouldMap() {
        final DescriptionListExtractionDefinition definition = Instancio.of(DescriptionListExtractionDefinition.class)
                .withSetting(COLLECTION_MAX_SIZE, 1)
                .create();

        final DescriptionListExtractionDataPointConfiguration dataPointConfiguration = mock();
        when(descriptionListExtractionDataPointConfigurationMapper.map(definition.getDataPoints().getFirst())).thenReturn(dataPointConfiguration);

        final DescriptionListExtractionConfiguration configuration = mapper.map(definition);

        assertSame(dataPointConfiguration, configuration.getDataPoints().getFirst());
    }
}