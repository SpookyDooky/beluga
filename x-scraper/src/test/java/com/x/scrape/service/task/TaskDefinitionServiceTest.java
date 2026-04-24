package com.x.scrape.service.task;

import com.x.scrape.execution.model.task.TaskDefinition;
import com.x.scrape.persistence.repository.TaskDefinitionRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.net.URI;
import java.net.URL;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TaskDefinitionServiceTest {
	
	@Mock
	private TaskDefinitionRepository taskDefinitionRepository;
	
	@InjectMocks
	private TaskDefinitionService taskDefinitionService;
	
	@Test
	void shouldFindAllActiveTaskDefinitionUrlsByJobDefinitionId() throws Exception {
		final Long jobDefinitionId = 123L;
		final String stringUrl = "http://localhost:1234";
		when(taskDefinitionRepository.findAllActiveTaskDefinitionUrlsByJobDefinitionId(jobDefinitionId)).thenReturn(Set.of(stringUrl));
		final Set<URL> expected = Set.of(URI.create(stringUrl).toURL());
		
		final Set<URL> result = taskDefinitionService.getActiveTaskDefinitionUrlsByJobDefinitionId(jobDefinitionId);
		
		assertEquals(expected, result);
	}
	
	@Test
	void shouldFindAllByActive() {
		final Long jobDefinitionId = 123L;
		final List<TaskDefinition> expected = List.of();
		when(taskDefinitionRepository.findAllByActiveAndJobDefinitionId(jobDefinitionId)).thenReturn(expected);
		
		final List<TaskDefinition> result = taskDefinitionService.getAllActiveByJobDefinitionId(jobDefinitionId);
		
		assertSame(expected, result);
	}
}