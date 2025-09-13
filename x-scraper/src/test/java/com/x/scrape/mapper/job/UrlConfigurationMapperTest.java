package com.x.scrape.mapper.job;

import com.x.scrape.model.job.UrlConfiguration;
import com.x.scrape.properties.scraping.url.UrlProperties;
import org.instancio.Instancio;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UrlConfigurationMapperTest {

	private final UrlConfigurationMapperImpl mapper = new UrlConfigurationMapperImpl();
	
	@Test
	void shouldMap() {
		final UrlProperties urlProperties = Instancio.create(UrlProperties.class);
		
		final UrlConfiguration urlConfiguration = mapper.map(urlProperties);
		
		assertEquals(urlProperties.getUrls(), urlConfiguration.getUrls());
		assertEquals(urlProperties.getUrlFile(), urlConfiguration.getUrlFile());
	}
}