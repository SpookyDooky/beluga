package com.beluga.mapper.task.configuration;

import com.beluga.execution.model.task.ScrapingConfiguration;
import com.beluga.execution.model.task.extraction_configuration.ExtractionConfiguration;
import com.beluga.mapper.task.configuration.extraction_configuration.ExtractionConfigurationMapper;
import com.beluga.model.job_definition.configuration.scraping_configuration.ScrapingDefinition;
import com.beluga.model.job_definition.configuration.scraping_configuration.extraction_definition.text.TextExtractionDefinition;
import org.instancio.Instancio;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.instancio.settings.Keys.COLLECTION_MAX_SIZE;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ScrapingConfigurationMapperTest {

    @Mock
    private ExtractionConfigurationMapper extractionConfigurationMapper;

    @InjectMocks
    private ScrapingConfigurationMapperImpl scrapingConfigurationMapper;

    @Test
    void shouldMap() {
        final ScrapingDefinition scrapingDefinition = Instancio.of(ScrapingDefinition.class)
                .withSetting(COLLECTION_MAX_SIZE, 1)
                .create();
        scrapingDefinition.getExtractionDefinitions().add(mock(TextExtractionDefinition.class));

        final ExtractionConfiguration extractionConfiguration = mock();
        when(extractionConfigurationMapper.map(scrapingDefinition.getExtractionDefinitions().getFirst())).thenReturn(extractionConfiguration);

        final ScrapingConfiguration scrapingConfiguration = scrapingConfigurationMapper.map(scrapingDefinition);

        assertEquals(scrapingDefinition.getItemSelector(),  scrapingConfiguration.getItemSelector());
        assertEquals(1, scrapingConfiguration.getExtractionConfigurations().size());
        assertTrue(scrapingConfiguration.getExtractionConfigurations().contains(extractionConfiguration));
    }

}