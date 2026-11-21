package com.beluga.service.job;

import com.beluga.logging.ContextLogger;
import com.beluga.mapper.job.JobDefinitionMapper;
import com.beluga.model.job_definition.JobDefinition;
import com.beluga.properties.BelugaScraperProperties;
import com.beluga.properties.scraping.JobProperties;
import org.instancio.Instancio;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.ApplicationArguments;

import java.util.List;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class JobRegistrarTest {
	
	@Mock
	private ContextLogger contextLogger;
	@Mock
	private BelugaScraperProperties belugaScraperProperties;
	@Mock
	private JobDefinitionMapper jobDefinitionMapper;
	@Mock
	private JobDefinitionService jobDefinitionService;
	
	@InjectMocks
	private JobRegistrar jobRegistrar;
	
	@Test
	void shouldRegisterJobs() {
		final List<JobProperties> jobPropertiesList = List.of(Instancio.create(JobProperties.class));
		when(belugaScraperProperties.getJobs()).thenReturn(jobPropertiesList);
		
		final JobDefinition jobDefinition = Instancio.create(JobDefinition.class);
		when(jobDefinitionMapper.map(jobPropertiesList.getFirst())).thenReturn(jobDefinition);
		
		jobRegistrar.registerJobs();
		
		verify(jobDefinitionService).save(jobDefinition);
	}

	@Test
	void shouldRegisterJobsWhenApplicationRuns() {
		final List<JobProperties> jobPropertiesList = List.of(Instancio.create(JobProperties.class));
		when(belugaScraperProperties.getJobs()).thenReturn(jobPropertiesList);
		
		final JobDefinition jobDefinition = Instancio.create(JobDefinition.class);
		when(jobDefinitionMapper.map(jobPropertiesList.getFirst())).thenReturn(jobDefinition);
		
		jobRegistrar.run(mock(ApplicationArguments.class));
		
		verify(jobDefinitionService).save(jobDefinition);
	}

	@Test
	void shouldOverwriteJob() {
		final List<JobProperties> jobPropertiesList = List.of(Instancio.create(JobProperties.class));
		when(belugaScraperProperties.getJobs()).thenReturn(jobPropertiesList);

		final JobDefinition jobDefinition = Instancio.create(JobDefinition.class);
		when(jobDefinitionMapper.map(jobPropertiesList.getFirst())).thenReturn(jobDefinition);

		when(jobDefinitionService.existsByName(jobDefinition.getName())).thenReturn(true);

		jobRegistrar.registerJobs();

		verify(jobDefinitionService).deleteByName(jobDefinition.getName());
		verify(jobDefinitionService).save(jobDefinition);
	}
}
