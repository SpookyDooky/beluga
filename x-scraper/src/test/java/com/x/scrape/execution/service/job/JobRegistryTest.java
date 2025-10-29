package com.x.scrape.execution.service.job;

import com.x.scrape.execution.model.Job;
import com.x.scrape.logging.ContextLogger;
import com.x.scrape.mapper.job.JobDefinitionMapper;
import com.x.scrape.model.job_definition.JobDefinition;
import com.x.scrape.properties.XScraperProperties;
import com.x.scrape.properties.scraping.JobProperties;
import com.x.scrape.service.JobDefinitionService;
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
class JobRegistryTest {
	
	@Mock
	private ContextLogger contextLogger;
	@Mock
	private XScraperProperties xScraperProperties;
	@Mock
	private JobDefinitionMapper jobDefinitionMapper;
	@Mock
	private JobExecutionService jobExecutionService;
	@Mock
	private JobDefinitionService jobDefinitionService;
	
	@InjectMocks
	private JobRegistry jobRegistry;
	
	@Test
	void shouldRegisterJobs() {
		final List<JobProperties> jobPropertiesList = List.of(Instancio.create(JobProperties.class));
		when(xScraperProperties.getJobs()).thenReturn(jobPropertiesList);
		
		final JobDefinition jobDefinition = Instancio.create(JobDefinition.class);
		when(jobDefinitionMapper.map(jobPropertiesList.getFirst())).thenReturn(jobDefinition);
		
		final Job job = mock();
		when(jobDefinitionService.createJobById(jobDefinition.getId())).thenReturn(job);
		
		jobRegistry.registerJobs();
		
		verify(jobExecutionService).executeJob(job);
	}
	
	@Test
	void shouldGetJob() {
		final List<JobProperties> jobPropertiesList = List.of(Instancio.create(JobProperties.class));
		when(xScraperProperties.getJobs()).thenReturn(jobPropertiesList);
		
		final JobDefinition jobDefinition = Instancio.create(JobDefinition.class);
		when(jobDefinitionMapper.map(jobPropertiesList.getFirst())).thenReturn(jobDefinition);
		
		final Job job = Instancio.create(Job.class);
		when(jobDefinitionService.createJobById(jobDefinition.getId())).thenReturn(job);
		
		jobRegistry.registerJobs();
		final Job result = jobRegistry.get(job.getId());

		assertSame(job, result);
	}
}