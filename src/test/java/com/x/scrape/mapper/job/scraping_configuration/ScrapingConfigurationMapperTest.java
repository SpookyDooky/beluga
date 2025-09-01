package com.x.scrape.mapper.job.scraping_configuration;

import com.x.scrape.model.job.scraping_configuration.DataPointConfiguration;
import com.x.scrape.model.job.scraping_configuration.ScrapingConfiguration;
import com.x.scrape.properties.scraping.DataPointProperties;
import com.x.scrape.properties.scraping.DataScrapingProperties;
import org.instancio.Instancio;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.instancio.Select.field;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ScrapingConfigurationMapperTest {

	@Mock
	private DataPointConfigurationMapper dataPointConfigurationMapper;
	
	@InjectMocks
	private ScrapingConfigurationMapperImpl mapper;
	
	@Test
	void shouldMap() {
		final DataScrapingProperties dataScrapingProperties = Instancio.of(DataScrapingProperties.class)
				.set(field(DataScrapingProperties::getDataPoints), List.of(mock(DataPointProperties.class)))
				.create();
		
		final DataPointConfiguration dataPointConfiguration = mock();
		when(dataPointConfigurationMapper.map(dataScrapingProperties.getDataPoints().getFirst())).thenReturn(dataPointConfiguration);
		
		final ScrapingConfiguration scrapingConfiguration = mapper.map(dataScrapingProperties);
		
		assertEquals(dataScrapingProperties.getElementSelector(), scrapingConfiguration.getElementSelector());
		assertEquals(1, scrapingConfiguration.getDataPointConfigurations().size());
		assertSame(dataPointConfiguration, scrapingConfiguration.getDataPointConfigurations().getFirst());
	}
}