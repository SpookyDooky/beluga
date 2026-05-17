package com.beluga.mapper.task.configuration;

import com.beluga.execution.model.task.StorageConfiguration;
import com.beluga.model.job_definition.configuration.storage_configuration.StorageDefinition;
import org.instancio.Instancio;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class StorageDefinitionMapperTest {

    private final StorageConfigurationMapperImpl storageConfigurationMapper = new StorageConfigurationMapperImpl();

    @Test
    void shouldMap() {
        final StorageDefinition storageDefinition = Instancio.create(StorageDefinition.class);

        final StorageConfiguration storageConfiguration =  storageConfigurationMapper.map(storageDefinition);

        assertEquals(storageDefinition.getFormat(), storageConfiguration.getFormat());
    }

}