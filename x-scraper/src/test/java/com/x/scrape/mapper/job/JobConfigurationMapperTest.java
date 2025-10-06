package com.x.scrape.mapper.job;

import com.x.scrape.mapper.job.scraping_configuration.ScrapingConfigurationMapper;
import com.x.scrape.mapper.job.storage.StorageConfigurationMapper;
import com.x.scrape.model.JobConfiguration;
import com.x.scrape.model.job.UrlConfiguration;
import com.x.scrape.model.job.scraping_configuration.ScrapingConfiguration;
import com.x.scrape.model.job.storage.StorageConfiguration;
import com.x.scrape.properties.scraping.JobProperties;
import org.instancio.Instancio;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class JobConfigurationMapperTest {
	
	@Mock
	private UrlConfigurationMapper urlConfigurationMapper;
	@Mock
	private ScrapingConfigurationMapper scrapingConfigurationMapper;
	@Mock
	private StorageConfigurationMapper storageConfigurationMapper;
	
	@InjectMocks
	private JobConfigurationMapperImpl mapper;
	
	@Test
	void shouldMap() {
		final JobProperties jobProperties = Instancio.create(JobProperties.class);
		
		final UrlConfiguration urlConfiguration = mock();
		when(urlConfigurationMapper.map(jobProperties.getUrl())).thenReturn(urlConfiguration);
		
		final ScrapingConfiguration scrapingConfiguration = mock();
		when(scrapingConfigurationMapper.map(jobProperties.getScraping())).thenReturn(scrapingConfiguration);
		
		final StorageConfiguration storageConfiguration = mock();
		when(storageConfigurationMapper.map(jobProperties.getStorage())).thenReturn(storageConfiguration);
		
		final JobConfiguration jobConfiguration = mapper.map(jobProperties);
		
		assertSame(urlConfiguration, jobConfiguration.getUrlConfiguration());
		assertSame(scrapingConfiguration, jobConfiguration.getScrapingConfiguration());
		assertSame(storageConfiguration, jobConfiguration.getStorageConfiguration());
	}
}