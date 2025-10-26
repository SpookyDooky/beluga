package com.x.scrape.mapper.job;

import com.x.scrape.mapper.job.execution.ExecutionConfigurationMapper;
import com.x.scrape.mapper.job.scraping_configuration.ScrapingConfigurationMapper;
import com.x.scrape.mapper.job.storage.StorageConfigurationMapper;
import com.x.scrape.model.job_definition.JobDefinition;
import com.x.scrape.model.job_definition.configuration.UrlConfiguration;
import com.x.scrape.model.job_definition.configuration.execution_configuration.ExecutionConfiguration;
import com.x.scrape.model.job_definition.configuration.scraping_configuration.ScrapingConfiguration;
import com.x.scrape.model.job_definition.configuration.storage_configuration.StorageConfiguration;
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
class JobDefinitionConfigurationMapperTest {
	
	@Mock
	private UrlConfigurationMapper urlConfigurationMapper;
	@Mock
	private ScrapingConfigurationMapper scrapingConfigurationMapper;
	@Mock
	private StorageConfigurationMapper storageConfigurationMapper;
	@Mock
	private ExecutionConfigurationMapper executionConfigurationMapper;
	
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
		
		final ExecutionConfiguration executionConfiguration = mock();
		when(executionConfigurationMapper.map(jobProperties.getExecution())).thenReturn(executionConfiguration);
		final JobDefinition jobConfiguration = mapper.map(jobProperties);
		
		assertSame(urlConfiguration, jobConfiguration.getJobConfiguration().getUrlConfiguration());
		assertSame(scrapingConfiguration, jobConfiguration.getJobConfiguration().getScrapingConfiguration());
		assertSame(storageConfiguration, jobConfiguration.getJobConfiguration().getStorageConfiguration());
		assertSame(executionConfiguration, jobConfiguration.getJobConfiguration().getExecutionConfiguration());
	}
}