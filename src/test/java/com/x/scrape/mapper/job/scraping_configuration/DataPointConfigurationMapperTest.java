package com.x.scrape.mapper.job.scraping_configuration;

import com.x.scrape.model.job.scraping_configuration.DataPointConfiguration;
import com.x.scrape.properties.scraping.DataPointProperties;
import org.instancio.Instancio;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class DataPointConfigurationMapperTest {
	
	private final DataPointConfigurationMapperImpl mapper = new DataPointConfigurationMapperImpl();
	
	@Test
	void shouldMap() {
		final DataPointProperties dataPointProperties = Instancio.create(DataPointProperties.class);
		
		final DataPointConfiguration dataPointConfiguration = mapper.map(dataPointProperties);
		
		assertEquals(dataPointProperties.getSelector(), dataPointConfiguration.getSelector());
		assertEquals(dataPointProperties.getPropertyName(), dataPointConfiguration.getPropertyName());
		assertEquals(dataPointProperties.getValueSelector(), dataPointConfiguration.getValueSelector());
	}
}