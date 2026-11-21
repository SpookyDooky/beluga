package com.beluga.service.task;

import com.beluga.execution.model.task.TaskDefinition;
import com.beluga.persistence.repository.TaskDefinitionRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TaskDefinitionServiceTest {
	
	@Mock
	private TaskDefinitionRepository taskDefinitionRepository;
	
	@InjectMocks
	private TaskDefinitionService taskDefinitionService;
	
	@Test
	void shouldFindAllActiveTaskDefinitionUrlsByJobDefinitionId() {
		final Long jobDefinitionId = 123L;
		final Set<String> expected = Set.of("http://localhost:1234");
		when(taskDefinitionRepository.findAllActiveTaskDefinitionUrlsByJobDefinitionId(jobDefinitionId)).thenReturn(expected);

		final Set<String> result = taskDefinitionService.getActiveUrlsByJobDefinitionId(jobDefinitionId);
		
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
	
	@Test
	void shouldGetAllActiveUrlsByJobDefinitionIdAndUrlIn() {
		final Long jobDefinitionId = 123L;
		final Set<String> expected = Set.of("http://localhost:1234");
		when(taskDefinitionRepository.getAllActiveUrlsByJobDefinitionIdAndInUrls(jobDefinitionId, expected)).thenReturn(expected);

		final Set<String> result = taskDefinitionService.getAllActiveUrlsByJobDefinitionIdAndUrlIn(expected, jobDefinitionId);
		
		assertEquals(expected, result);
	}
	
	@Test
	void shouldSetAllToInactiveByJobDefinitionIdAndUrlNotInUrls() {
		final Long jobDefinitionId = 123L;
		final Set<String> urls = Set.of();
		
		taskDefinitionService.setAllToInactiveByJobDefinitionIdAndUrlNotInUrls(jobDefinitionId, urls);
		
		verify(taskDefinitionRepository).setActiveFalseByJobDefinitionIdAndUrlNotInUrls(jobDefinitionId, urls);
	}
}