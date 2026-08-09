package com.beluga.mapper.task.configuration.extraction_configuration.description_list;

import com.beluga.execution.model.task.extraction_configuration.description_list.DescriptionListExtractionDataPointConfiguration;
import com.beluga.model.job_definition.configuration.scraping_configuration.extraction_definition.description_list.DescriptionListExtractionDataPointDefinition;
import org.instancio.Instancio;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DescriptionListExtractionDataPointConfigurationMapperTest {

    private final DescriptionListExtractionDataPointConfigurationMapper mapper = new DescriptionListExtractionDataPointConfigurationMapperImpl();

    @Test
    void shouldMap() {
        final DescriptionListExtractionDataPointDefinition definition = Instancio.create(DescriptionListExtractionDataPointDefinition.class);

        final DescriptionListExtractionDataPointConfiguration configuration = mapper.map(definition);

        assertEquals(definition.getDtValue(), configuration.getDtValue());
        assertEquals(definition.getField(), configuration.getField());
    }
}