package com.beluga.mapper.task.configuration;

import com.beluga.execution.model.task.DataPointConfiguration;
import com.beluga.execution.model.task.ScrapingConfiguration;
import com.beluga.model.job_definition.configuration.scraping_configuration.ScrapingDefinition;
import org.instancio.Instancio;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.instancio.settings.Keys.COLLECTION_MAX_SIZE;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ScrapingDefinitionMapperTest {

    @Mock
    private DataPointConfigurationMapper dataPointConfigurationMapper;

    @InjectMocks
    private ScrapingConfigurationMapperImpl scrapingConfigurationMapper;

    @Test
    void shouldMap() {
        final ScrapingDefinition scrapingDefinition = Instancio.of(ScrapingDefinition.class)
                .withSetting(COLLECTION_MAX_SIZE, 1)
                .create();
        final DataPointConfiguration dataPointConfiguration = mock();
        when(dataPointConfigurationMapper.map(scrapingDefinition.getDataPointDefinitions().getFirst())).thenReturn(dataPointConfiguration);

        final ScrapingConfiguration scrapingConfiguration = scrapingConfigurationMapper.map(scrapingDefinition);

        assertEquals(scrapingDefinition.getItemSelector(),  scrapingConfiguration.getItemSelector());
        assertEquals(1, scrapingConfiguration.getDataPointConfigurations().size());
        assertTrue(scrapingConfiguration.getDataPointConfigurations().contains(dataPointConfiguration));
    }

}