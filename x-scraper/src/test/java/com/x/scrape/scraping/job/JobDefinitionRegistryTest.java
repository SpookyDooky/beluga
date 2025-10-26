package com.x.scrape.scraping.job;

import com.x.scrape.logging.ContextLogger;
import com.x.scrape.mapper.job.JobConfigurationMapper;
import com.x.scrape.model.JobConfiguration;
import com.x.scrape.model.job.JobDefinition;
import com.x.scrape.properties.XScraperProperties;
import com.x.scrape.properties.scraping.JobProperties;
import org.instancio.Instancio;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class JobDefinitionRegistryTest {
	
	@Mock
	private ContextLogger contextLogger;
	@Mock
	private XScraperProperties xScraperProperties;
	@Mock
	private JobConfigurationMapper jobConfigurationMapper;
	@Mock
	private JobExecutionService jobExecutionService;
	
	@InjectMocks
	private JobRegistry jobRegistry;
	
	@Test
	void shouldRegisterJobs() {
		final List<JobProperties> jobPropertiesList = List.of(Instancio.create(JobProperties.class));
		when(xScraperProperties.getJobs()).thenReturn(jobPropertiesList);
		
		final JobConfiguration jobConfiguration = mock();
		when(jobConfigurationMapper.map(jobPropertiesList.getFirst())).thenReturn(jobConfiguration);
		
		final JobDefinition jobDefinition = Instancio.create(JobDefinition.class);
		when(jobConfiguration.getJob()).thenReturn(jobDefinition);
		
		jobRegistry.registerJobs();
		
		verify(jobExecutionService).executeJob(jobDefinition);
	}
	
	@Test
	void shouldGetJob() {
		final List<JobProperties> jobPropertiesList = List.of(Instancio.create(JobProperties.class));
		when(xScraperProperties.getJobs()).thenReturn(jobPropertiesList);
		
		final JobConfiguration jobConfiguration = mock();
		when(jobConfigurationMapper.map(jobPropertiesList.getFirst())).thenReturn(jobConfiguration);
		
		final JobDefinition jobDefinition = Instancio.create(JobDefinition.class);
		when(jobConfiguration.getJob()).thenReturn(jobDefinition);
		
		jobRegistry.registerJobs();
		final JobDefinition result = jobRegistry.get(jobDefinition.getUuid());

		assertSame(jobDefinition, result);
	}
}