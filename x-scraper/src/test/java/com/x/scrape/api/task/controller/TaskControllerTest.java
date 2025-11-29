package com.x.scrape.api.task.controller;

import com.x.scrape.api.task.dto.ReadTaskDefinitionDto;
import com.x.scrape.api.task.dto.UpdateTaskDto;
import com.x.scrape.api.task.mapper.ReadTaskDefinitionDtoMapper;
import com.x.scrape.logging.ContextLogger;
import com.x.scrape.mapper.task.TaskDefinitionMapperService;
import com.x.scrape.model.job_definition.JobDefinition;
import com.x.scrape.model.task.TaskDefinition;
import com.x.scrape.service.JobDefinitionService;
import com.x.scrape.service.exception.JobDefinitionNotFoundException;
import com.x.scrape.test_utils.TestReflectionUtility;
import org.instancio.Instancio;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.List;

import static org.instancio.Select.field;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TaskControllerTest {
	
	@Mock
	private ContextLogger logger;
	@Mock
	private TaskDefinitionMapperService taskDefinitionMapperService;
	@Mock
	private JobDefinitionService jobDefinitionService;
	@Mock
	private ReadTaskDefinitionDtoMapper readTaskDefinitionDtoMapper;
	
	@InjectMocks
	private TaskController taskController;
	
	@Test
	void shouldGetTasks() {
		final List<TaskDefinition> taskDefinitions = List.of(mock(TaskDefinition.class));
		final ReadTaskDefinitionDto readTaskDefinitionDto = mock();
		when(readTaskDefinitionDtoMapper.map(taskDefinitions.getFirst())).thenReturn(readTaskDefinitionDto);
		
		final JobDefinition jobDefinition = Instancio.of(JobDefinition.class)
				.set(field(JobDefinition::getTaskDefinitions), taskDefinitions)
				.create();
		when(jobDefinitionService.getById(jobDefinition.getId())).thenReturn(jobDefinition);
		
		final List<ReadTaskDefinitionDto> result = taskController.getTasks(jobDefinition.getId());
		
		assertEquals(1, result.size());
		assertTrue(result.contains(readTaskDefinitionDto));
	}
	
	@Test
	void shouldHandleJobDefinitionNotFoundException() {
		final ResponseEntity<Void> result = taskController.handleJobDefinitionNotFound();
		
		assertEquals(404, result.getStatusCode().value());
	}
	
	@Test
	void shouldHaveExceptionHandlerOnHandleJobDefinitionNotFound() {
		final ExceptionHandler exceptionHandler = TestReflectionUtility.assertAnnotationPresentOnMethod(
				TaskController.class,
				ExceptionHandler.class,
				"handleJobDefinitionNotFound"
		);
		
		assertEquals(1, exceptionHandler.value().length);
		assertEquals(JobDefinitionNotFoundException.class, exceptionHandler.value()[0]);
	}
	
	@Test
	void shouldUpdateTasks() {
		final Long jobId = 123L;
		final UpdateTaskDto updateTaskDto = Instancio.create(UpdateTaskDto.class);
		
		final List<TaskDefinition> taskDefinitions = List.of(mock(TaskDefinition.class));
		when(taskDefinitionMapperService.map(updateTaskDto.getUrls())).thenReturn(taskDefinitions);
		
		final JobDefinition jobDefinition = Instancio.create(JobDefinition.class);
		when(jobDefinitionService.getById(jobId)).thenReturn(jobDefinition);
		
		final ReadTaskDefinitionDto expectedTaskDefinition = mock();
		when(readTaskDefinitionDtoMapper.map(taskDefinitions.get(0))).thenReturn(expectedTaskDefinition);
		
		final List<ReadTaskDefinitionDto> result = taskController.updateTasks(jobId, updateTaskDto);
		
		verify(jobDefinitionService).save(jobDefinition);
		
		assertEquals(1, result.size());
		assertTrue(result.contains(expectedTaskDefinition));
	}
}