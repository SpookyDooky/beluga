package com.x.scrape.mapper.job.storage;

import com.x.scrape.model.job.storage.StorageConfiguration;
import com.x.scrape.properties.scraping.storage.FileProperties;
import org.instancio.Instancio;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class StorageConfigurationMapperTest {

	private final StorageConfigurationMapperImpl mapper = new StorageConfigurationMapperImpl();
	
	@Test
	void shouldMap() {
		final FileProperties fileProperties = Instancio.create(FileProperties.class);
		
		final StorageConfiguration storageConfiguration = mapper.map(fileProperties);
		
		assertEquals(fileProperties.getFormat(), storageConfiguration.getFormat());
		assertEquals(fileProperties.getFile(), storageConfiguration.getFile());
		assertEquals(fileProperties.getFolder(), storageConfiguration.getFolder());
	}
}