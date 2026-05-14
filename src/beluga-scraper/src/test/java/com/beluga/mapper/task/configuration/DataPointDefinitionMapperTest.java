package com.beluga.mapper.task.configuration;

import com.beluga.execution.model.task.DataPointConfiguration;
import com.beluga.model.job_definition.configuration.scraping_configuration.DataPointDefinition;
import org.instancio.Instancio;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DataPointDefinitionMapperTest {

    private final DataPointConfigurationMapper mapper = new DataPointConfigurationMapperImpl();

    @Test
    void shouldMap() {
        final DataPointDefinition dataPointDefinition = Instancio.create(DataPointDefinition.class);

        final DataPointConfiguration dataPointConfiguration = mapper.map(dataPointDefinition);
        assertEquals(dataPointDefinition.getField(),  dataPointConfiguration.getField());
        assertEquals(dataPointDefinition.getSelector(), dataPointConfiguration.getSelector());
        assertEquals(dataPointDefinition.getAttribute(), dataPointConfiguration.getAttribute());
        assertEquals(dataPointDefinition.getType(), dataPointConfiguration.getType());

    }

}