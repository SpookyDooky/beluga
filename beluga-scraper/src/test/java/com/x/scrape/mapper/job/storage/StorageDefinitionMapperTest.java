package com.x.scrape.mapper.job.storage;

import com.x.scrape.api.job.dto.read.ReadStorageConfigurationDto;
import com.x.scrape.api.job.dto.write.WriteStorageConfigurationDto;
import com.x.scrape.model.job_definition.configuration.storage_configuration.StorageDefinition;
import com.x.scrape.properties.scraping.storage.StorageProperties;
import org.instancio.Instancio;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class StorageDefinitionMapperTest {

	private final StorageConfigurationMapperImpl mapper = new StorageConfigurationMapperImpl();
	
	@Test
	void shouldMap() {
		final StorageProperties storageProperties = Instancio.create(StorageProperties.class);
		
		final StorageDefinition storageDefinition = mapper.map(storageProperties);
		
		assertEquals(storageProperties.getFormat(), storageDefinition.getFormat());
		assertEquals(storageProperties.getFolder(), storageDefinition.getFolder());
	}
	
	@Test
	void shouldMapFromWriteStorageConfigurationDto() {
		final WriteStorageConfigurationDto dto = Instancio.create(WriteStorageConfigurationDto.class);
		
		final StorageDefinition entity = mapper.map(dto);
		
		assertEquals(dto.getFolder(), entity.getFolder());
	}
	
	@Test
	void shouldMapToDto() {
		final StorageDefinition entity = Instancio.create(StorageDefinition.class);
		
		final ReadStorageConfigurationDto dto = mapper.map(entity);
		
		assertEquals(entity.getId(), dto.getId());
		assertEquals(entity.getFolder(), dto.getFolder());
	}
	
	@Test
	void shouldUpdate() {
		final WriteStorageConfigurationDto dto = Instancio.create(WriteStorageConfigurationDto.class);
		final StorageDefinition entity = Instancio.create(StorageDefinition.class);
		
		mapper.update(dto, entity);
		
		assertEquals(dto.getFolder(), entity.getFolder());
	}
}