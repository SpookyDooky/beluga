package com.beluga.mapper.job;

import com.beluga.execution.model.job.Job;
import com.beluga.model.job_definition.JobDefinition;
import org.instancio.Instancio;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class JobMapperTest {

	private final JobMapper mapper = new JobMapperImpl();
	
	@Test
	void shouldMap() {
		final JobDefinition jobDefinition = Instancio.create(JobDefinition.class);
		
		final Job job = mapper.map(jobDefinition);
		
		assertEquals(jobDefinition.getId(), job.getJobDefinitionId());
		assertEquals(jobDefinition.getName(), job.getJobName());
		assertEquals(jobDefinition.getExecutionDefinition().getTasksPerSecond(), job.getExecutionConfiguration().getTasksPerSecond());
	}
}