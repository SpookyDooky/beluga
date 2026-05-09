package com.beluga.mapper.task;

import com.beluga.execution.model.task.TaskDefinition;
import com.beluga.properties.scraping.url.UrlProperties;
import org.instancio.Instancio;
import org.junit.jupiter.api.Test;

import java.net.URL;
import java.util.List;

import static org.instancio.Select.field;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

class TaskDefinitionMapperServiceTest {
	
	private final TaskDefinitionMapperService taskDefinitionMapperService = new TaskDefinitionMapperService();
	
	@Test
	void shouldMapUrls() {
		final URL url = mock();
		final UrlProperties urlProperties = Instancio.of(UrlProperties.class)
				.ignore(field(UrlProperties::getUrlFile))
				.set(field(UrlProperties::getUrls), List.of(url))
				.create();
		
		final List<TaskDefinition> taskDefinitions = taskDefinitionMapperService.map(urlProperties);
		
		assertEquals(1, taskDefinitions.size());
		
		final TaskDefinition taskDefinition = taskDefinitions.get(0);
		assertSame(url, taskDefinition.getUrl());
	}
	
	@Test
	void shouldMapFromFile() {
		final String urlFile = TaskDefinitionMapperServiceTest.class
				.getClassLoader()
				.getResource("test-files/url-file.txt")
				.getPath()
				.replaceFirst("/C:", "");
		
		final UrlProperties urlProperties = Instancio.of(UrlProperties.class)
				.ignore(field(UrlProperties::getUrls))
				.set(field(UrlProperties::getUrlFile), urlFile)
				.create();
		
		final List<TaskDefinition> taskDefinitions = taskDefinitionMapperService.map(urlProperties);
		
		assertEquals(1, taskDefinitions.size());
		
		final TaskDefinition taskDefinition = taskDefinitions.get(0);
		assertEquals("https://not-a-real-url.com", taskDefinition.getUrl().toString());
	}
}