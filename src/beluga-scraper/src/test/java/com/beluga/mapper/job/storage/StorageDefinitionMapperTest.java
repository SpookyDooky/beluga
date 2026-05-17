package com.beluga.mapper.job.storage;

import com.beluga.model.job_definition.configuration.storage_configuration.StorageDefinition;
import com.beluga.properties.scraping.storage.StorageProperties;
import org.instancio.Instancio;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class StorageDefinitionMapperTest {

	private final StorageDefinitionMapperImpl mapper = new StorageDefinitionMapperImpl();
	
	@Test
	void shouldMap() {
		final StorageProperties storageProperties = Instancio.create(StorageProperties.class);
		
		final StorageDefinition storageDefinition = mapper.map(storageProperties);
		
		assertEquals(storageProperties.getFormat(), storageDefinition.getFormat());
	}
}