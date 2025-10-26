package com.x.scrape.mapper.job.storage;

import com.x.scrape.model.job.configuration.storage.StorageConfiguration;
import com.x.scrape.properties.scraping.storage.StorageProperties;
import org.instancio.Instancio;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class StorageConfigurationMapperTest {

	private final StorageConfigurationMapperImpl mapper = new StorageConfigurationMapperImpl();
	
	@Test
	void shouldMap() {
		final StorageProperties storageProperties = Instancio.create(StorageProperties.class);
		
		final StorageConfiguration storageConfiguration = mapper.map(storageProperties);
		
		assertEquals(storageProperties.getFormat(), storageConfiguration.getFormat());
		assertEquals(storageProperties.getFile(), storageConfiguration.getFile());
		assertEquals(storageProperties.getFolder(), storageConfiguration.getFolder());
	}
}