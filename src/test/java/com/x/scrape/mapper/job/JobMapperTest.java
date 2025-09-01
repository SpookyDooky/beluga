package com.x.scrape.mapper.job;

import com.x.scrape.mapper.job.scraping_configuration.ScrapingConfigurationMapper;
import com.x.scrape.mapper.job.storage.StorageConfigurationMapper;
import com.x.scrape.model.job.Job;
import com.x.scrape.model.job.UrlConfiguration;
import com.x.scrape.model.job.scraping_configuration.ScrapingConfiguration;
import com.x.scrape.model.job.storage.StorageConfiguration;
import com.x.scrape.properties.scraping.ScrapingProperties;
import org.instancio.Instancio;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class JobMapperTest {
	
	@Mock
	private UrlConfigurationMapper urlConfigurationMapper;
	@Mock
	private ScrapingConfigurationMapper scrapingConfigurationMapper;
	@Mock
	private StorageConfigurationMapper storageConfigurationMapper;
	
	@InjectMocks
	private JobMapperImpl mapper;
	
	@Test
	void shouldMap() {
		final ScrapingProperties scrapingProperties = Instancio.create(ScrapingProperties.class);
		
		final UrlConfiguration urlConfiguration = mock();
		when(urlConfigurationMapper.map(scrapingProperties.getUrl())).thenReturn(urlConfiguration);
		
		final ScrapingConfiguration scrapingConfiguration = mock();
		when(scrapingConfigurationMapper.map(scrapingProperties.getDataScraping())).thenReturn(scrapingConfiguration);
		
		final StorageConfiguration storageConfiguration = mock();
		when(storageConfigurationMapper.map(scrapingProperties.getStorage())).thenReturn(storageConfiguration);
		
		final Job job = mapper.map(scrapingProperties);
		
		assertSame(urlConfiguration, job.getUrlConfiguration());
		assertSame(scrapingConfiguration, job.getScrapingConfiguration());
		assertSame(storageConfiguration, job.getStorageConfiguration());
	}
}