package com.beluga.mapper.task.configuration;

import com.beluga.execution.model.task.DataPointConfiguration;
import com.beluga.execution.model.task.extraction_configuration.ExtractionConfiguration;
import com.beluga.mapper.task.configuration.extraction_configuration.ExtractionConfigurationMapper;
import com.beluga.model.job_definition.configuration.scraping_configuration.DataPointDefinition;
import org.instancio.Instancio;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DataPointConfigurationMapperTest {

    @Mock
    private ExtractionConfigurationMapper extractionConfigurationMapper;

    @InjectMocks
    private DataPointConfigurationMapperImpl mapper;

    @Test
    void shouldMap() {
        final DataPointDefinition dataPointDefinition = Instancio.create(DataPointDefinition.class);

        final ExtractionConfiguration extractionConfiguration = mock();
        when(extractionConfigurationMapper.map(dataPointDefinition.getExtractionDefinition())).thenReturn(extractionConfiguration);

        final DataPointConfiguration dataPointConfiguration = mapper.map(dataPointDefinition);

        assertEquals(dataPointDefinition.getSelector(), dataPointConfiguration.getSelector());
        assertSame(extractionConfiguration, dataPointConfiguration.getExtractionConfiguration());
    }
}