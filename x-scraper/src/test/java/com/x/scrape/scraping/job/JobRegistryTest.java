package com.x.scrape.scraping.job;

import com.x.scrape.mapper.job.JobMapper;
import com.x.scrape.model.job.Job;
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
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class JobRegistryTest {
	
	@Mock
	private XScraperProperties xScraperProperties;
	@Mock
	private JobMapper jobMapper;
	@Mock
	private JobExecutionService jobExecutionService;
	
	@InjectMocks
	private JobRegistry jobRegistry;
	
	@Test
	void shouldRegisterJobs() {
		final List<JobProperties> jobPropertiesList = List.of(Instancio.create(JobProperties.class));
		when(xScraperProperties.getJobs()).thenReturn(jobPropertiesList);
		
		final Job job = Instancio.create(Job.class);
		when(jobMapper.map(jobPropertiesList.getFirst())).thenReturn(job);
		
		jobRegistry.registerJobs();
		
		verify(jobExecutionService).executeJob(job);
	}
	
	@Test
	void shouldGetJob() {
		final List<JobProperties> jobPropertiesList = List.of(Instancio.create(JobProperties.class));
		when(xScraperProperties.getJobs()).thenReturn(jobPropertiesList);
		
		final Job job = Instancio.create(Job.class);
		when(jobMapper.map(jobPropertiesList.getFirst())).thenReturn(job);
		
		jobRegistry.registerJobs();
		final Job result = jobRegistry.get(job.getId());
		
		assertSame(job, result);
	}
}