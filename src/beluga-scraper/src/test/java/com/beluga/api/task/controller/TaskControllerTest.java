package com.beluga.api.task.controller;

import com.beluga.api.task.dto.PatchTaskDto;
import com.beluga.api.task.dto.ReadTaskDefinitionDto;
import com.beluga.api.task.dto.UpdateTaskDto;
import com.beluga.api.task.mapper.ReadTaskDefinitionDtoMapper;
import com.beluga.execution.model.task.TaskDefinition;
import com.beluga.logging.ContextLogger;
import com.beluga.mapper.task.TaskDefinitionMapperService;
import com.beluga.model.job_definition.JobDefinition;
import com.beluga.model.job_definition.exception.TaskDefinitionNotFoundException;
import com.beluga.service.exception.JobDefinitionNotFoundException;
import com.beluga.service.job.JobDefinitionService;
import com.beluga.service.task.TaskDefinitionService;
import com.beluga.test_utils.TestReflectionUtility;
import org.instancio.Instancio;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.net.URL;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Answers.RETURNS_DEEP_STUBS;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TaskControllerTest {
	
	@Mock(answer = RETURNS_DEEP_STUBS)
	private ContextLogger logger;
	@Mock
	private TaskDefinitionMapperService taskDefinitionMapperService;
	@Mock
	private JobDefinitionService jobDefinitionService;
	@Mock
	private ReadTaskDefinitionDtoMapper readTaskDefinitionDtoMapper;
	@Mock
	private TaskDefinitionService taskDefinitionService;
	
	@InjectMocks
	private TaskController taskController;
	
	@Test
	void shouldGetTasks() {
		final List<TaskDefinition> taskDefinitions = List.of(mock(TaskDefinition.class));
		final ReadTaskDefinitionDto readTaskDefinitionDto = mock();
		when(readTaskDefinitionDtoMapper.map(taskDefinitions.getFirst())).thenReturn(readTaskDefinitionDto);
		
		final JobDefinition jobDefinition = mock();
		when(jobDefinition.getActiveTaskDefinitions()).thenReturn(taskDefinitions);
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
		
		assertEquals(2, exceptionHandler.value().length);
		assertEquals(JobDefinitionNotFoundException.class, exceptionHandler.value()[0]);
		assertEquals(TaskDefinitionNotFoundException.class, exceptionHandler.value()[1]);
	}
	
	@Test
	void shouldGetTask() {
		final Long jobId = 123L;
		final JobDefinition jobDefinition = mock();
		when(jobDefinitionService.getById(jobId)).thenReturn(jobDefinition);
		
		final Long taskId = 321L;
		final TaskDefinition taskDefinition = mock();
		when(jobDefinition.getTaskDefinitionById(taskId)).thenReturn(taskDefinition);
		
		final ReadTaskDefinitionDto expected = mock();
		when(readTaskDefinitionDtoMapper.map(taskDefinition)).thenReturn(expected);
		
		final ReadTaskDefinitionDto result = taskController.getTask(jobId, taskId);
		
		assertSame(expected, result);
	}
	
	@Test
	void shouldUpdateTasks() {
		final Long jobDefinitionId = 123L;
		
		final URL newUrl = Instancio.create(URL.class);
		final URL existingUrl = Instancio.create(URL.class);
		when(taskDefinitionService.getAllActiveUrlsByJobDefinitionIdAndUrlIn(
				Set.of(newUrl, existingUrl),
				jobDefinitionId
		)).thenReturn(Set.of(existingUrl));
		
		final List<TaskDefinition> newTaskDefinitions = List.of(mock(TaskDefinition.class));
		when(taskDefinitionMapperService.map(Set.of(newUrl))).thenReturn(newTaskDefinitions);
		
		final JobDefinition jobDefinition = mock();
		when(jobDefinitionService.getById(jobDefinitionId)).thenReturn(jobDefinition);
		
		final List<ReadTaskDefinitionDto> expected = List.of(mock(ReadTaskDefinitionDto.class));
		when(jobDefinition.getActiveTaskDefinitions()).thenReturn(newTaskDefinitions);
		when(readTaskDefinitionDtoMapper.map(newTaskDefinitions.getFirst())).thenReturn(expected.getFirst());
		
		final UpdateTaskDto updateTaskDto = new UpdateTaskDto();
		updateTaskDto.getUrls().addAll(Set.of(newUrl, existingUrl));
		
		final List<ReadTaskDefinitionDto> result = taskController.updateTasks(jobDefinitionId, updateTaskDto);
		
		verify(taskDefinitionService).setAllToInactiveByJobDefinitionIdAndUrlNotInUrls(jobDefinitionId, Set.of(existingUrl));
		verify(jobDefinition).addTaskDefinitions(newTaskDefinitions);
		verify(jobDefinitionService).save(jobDefinition);
		
		assertEquals(expected, result);
	}
	
	@Test
	void shouldUpdateTasksWithPatch() {
		final Long jobId = 123L;
		final PatchTaskDto patchTaskDto = Instancio.create(PatchTaskDto.class);
		
		final List<TaskDefinition> taskDefinitions = List.of(mock(TaskDefinition.class));
		when(taskDefinitionMapperService.map(patchTaskDto.getAdd())).thenReturn(taskDefinitions);
		
		final ReadTaskDefinitionDto readTaskDefinitionDto = mock();
		when(readTaskDefinitionDtoMapper.map(taskDefinitions.getFirst())).thenReturn(readTaskDefinitionDto);
		
		when(jobDefinitionService.getActiveTaskDefinitionsById(jobId)).thenReturn(taskDefinitions);
		
		final List<ReadTaskDefinitionDto> result = taskController.updateTasks(jobId, patchTaskDto);
		
		assertEquals(1, result.size());
		assertTrue(result.contains(readTaskDefinitionDto));
		
		verify(jobDefinitionService).setTaskDefinitionsInactiveByUrl(jobId, patchTaskDto.getRemove());
		verify(jobDefinitionService).addTaskDefinitionsById(
				taskDefinitions,
				jobId
		);
	}
}