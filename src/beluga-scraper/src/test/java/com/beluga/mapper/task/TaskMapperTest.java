package com.beluga.mapper.task;

import com.beluga.execution.model.task.ScrapingConfiguration;
import com.beluga.mapper.task.configuration.ScrapingConfigurationMapper;
import com.beluga.model.job_definition.JobDefinition;
import com.beluga.execution.model.task.Task;
import com.beluga.execution.model.task.TaskDefinition;
import org.instancio.Instancio;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.instancio.settings.Keys.COLLECTION_MAX_SIZE;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TaskMapperTest {

	@Mock
	private ScrapingConfigurationMapper scrapingConfigurationMapper;

	@InjectMocks
	private TaskMapperImpl mapper;
	
	@Test
	void shouldMap() {
		final TaskDefinition taskDefinition = Instancio.create(TaskDefinition.class);
		final JobDefinition jobDefinition = Instancio.of(JobDefinition.class)
				.withSetting(COLLECTION_MAX_SIZE, 1)
				.create();

		final ScrapingConfiguration scrapingConfiguration = mock();
		when(scrapingConfigurationMapper.map(jobDefinition.getScrapingDefinition())).thenReturn(scrapingConfiguration);

		final Task task = mapper.map(jobDefinition, taskDefinition);
		
		assertEquals(taskDefinition.getUrl(), task.getUrl());
		assertSame(scrapingConfiguration, task.getScrapingConfiguration());
	}
	
}