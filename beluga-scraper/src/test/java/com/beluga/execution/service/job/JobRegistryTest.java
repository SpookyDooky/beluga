package com.beluga.execution.service.job;

import com.beluga.execution.model.job.Job;
import com.beluga.logging.ContextLogger;
import com.beluga.mapper.job.JobDefinitionMapper;
import com.beluga.model.job_definition.JobDefinition;
import com.beluga.properties.scraping.JobProperties;
import com.beluga.service.job.JobDefinitionService;
import com.beluga.service.job.JobService;
import com.beluga.util.TimingService;
import org.instancio.Instancio;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.Answers.RETURNS_DEEP_STUBS;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class JobRegistryTest {
	
	@Mock
	private ContextLogger contextLogger;
	@Mock
	private com.beluga.properties.BelugaScraperProperties belugaScraperProperties;
	@Mock
	private JobDefinitionMapper jobDefinitionMapper;
	@Mock
	private JobExecutionService jobExecutionService;
	@Mock
	private JobDefinitionService jobDefinitionService;
	@Mock
	private JobService jobService;
	@Mock(answer = RETURNS_DEEP_STUBS)
	private TimingService timingService;
	
	@InjectMocks
	private JobRegistry jobRegistry;
	
	@Test
	void shouldRegisterJobs() {
		final List<JobProperties> jobPropertiesList = List.of(Instancio.create(JobProperties.class));
		when(belugaScraperProperties.getJobs()).thenReturn(jobPropertiesList);
		
		final JobDefinition jobDefinition = Instancio.create(JobDefinition.class);
		when(jobDefinitionMapper.map(jobPropertiesList.getFirst())).thenReturn(jobDefinition);
		
		final Job job = Instancio.create(Job.class);
		when(jobService.createJobByJobDefinitionId(jobDefinition.getId())).thenReturn(job);
		
		jobRegistry.registerJobs();
		
		verify(jobExecutionService).executeJob(job);
	}
	
	@Test
	void shouldGetJob() {
		final List<JobProperties> jobPropertiesList = List.of(Instancio.create(JobProperties.class));
		when(belugaScraperProperties.getJobs()).thenReturn(jobPropertiesList);
		
		final JobDefinition jobDefinition = Instancio.create(JobDefinition.class);
		when(jobDefinitionMapper.map(jobPropertiesList.getFirst())).thenReturn(jobDefinition);
		
		final Job job = Instancio.create(Job.class);
		when(jobService.createJobByJobDefinitionId(jobDefinition.getId())).thenReturn(job);
		
		jobRegistry.registerJobs();
		final Job result = jobRegistry.get(job.getId());

		assertSame(job, result);
	}
}