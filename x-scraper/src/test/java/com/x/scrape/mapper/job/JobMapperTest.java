package com.x.scrape.mapper.job;

import com.x.scrape.execution.model.Job;
import com.x.scrape.model.job_definition.JobDefinition;
import org.instancio.Instancio;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class JobMapperTest {

	private final JobMapper mapper = new JobMapperImpl();
	
	@Test
	void shouldMap() {
		final JobDefinition jobDefinition = Instancio.create(JobDefinition.class);
		
		final Job job = mapper.map(jobDefinition);
		
		assertEquals(jobDefinition.getId(), job.getJobDefinitionId());
		assertEquals(jobDefinition.getName(), job.getJobName());
		assertEquals(jobDefinition.getScrapingConfiguration(), job.getScrapingConfiguration());
		assertEquals(jobDefinition.getExecutionConfiguration(), job.getExecutionConfiguration());
		assertEquals(jobDefinition.getStorageConfiguration(), job.getStorageConfiguration());
		assertEquals(jobDefinition.getUrlConfiguration(), job.getUrlConfiguration());
	}
}