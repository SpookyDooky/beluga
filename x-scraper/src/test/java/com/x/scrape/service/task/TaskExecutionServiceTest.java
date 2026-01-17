package com.x.scrape.service.task;

import com.x.scrape.model.result.ResultFile;
import com.x.scrape.model.task.TaskExecution;
import com.x.scrape.model.task.TaskStatus;
import com.x.scrape.persistence.repository.TaskExecutionRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static com.x.scrape.model.task.TaskStatus.COMPLETED;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TaskExecutionServiceTest {
	
	@Mock
	private TaskExecutionRepository taskExecutionRepository;
	
	@InjectMocks
	private TaskExecutionService taskExecutionService;
	
	@Test
	void shouldFindById() {
		final Long id = 123L;
		final TaskExecution taskExecution = mock();
		when(taskExecutionRepository.findById(id)).thenReturn(Optional.of(taskExecution));
		
		final TaskExecution result = taskExecutionService.findById(id)
				.get();
		
		assertSame(taskExecution, result);
	}
	
	@Test
	void shouldNotFindById() {
		final Long id = 123L;
		when(taskExecutionRepository.findById(id)).thenReturn(Optional.empty());
		
		assertTrue(taskExecutionService.findById(id).isEmpty());
	}
	
	@Test
	void shouldGetById() {
		final Long id = 123L;
		final TaskExecution taskExecution = mock();
		when(taskExecutionRepository.findById(id)).thenReturn(Optional.of(taskExecution));
		
		final TaskExecution result = taskExecutionService.getById(id);
		
		assertSame(taskExecution, result);
	}
	
	@Test
	void shouldThrowEntityNotFoundExceptionGetById() {
		assertThrows(EntityNotFoundException.class, () -> {
			taskExecutionService.getById(123L);
		});
	}
	
	@Test
	void shouldSave() {
		final TaskExecution taskExecution = mock();
		when(taskExecutionRepository.save(taskExecution)).thenReturn(taskExecution);
		
		final TaskExecution result = taskExecutionService.save(taskExecution);
		
		assertSame(taskExecution, result);
	}
	
	@Test
	void shouldSetStatusById() {
		final Long id = 123L;
		final TaskExecution taskExecution = mock();
		when(taskExecutionRepository.findById(id)).thenReturn(Optional.of(taskExecution));
		
		final TaskStatus status = COMPLETED;
		taskExecutionService.setStatusById(id, status);
		
		verify(taskExecution).setStatus(status);
		verify(taskExecutionRepository).save(taskExecution);
	}
	
	@Test
	void shouldAddResultFile() {
		final Long id = 123L;
		final TaskExecution taskExecution = mock();
		when(taskExecutionRepository.findById(id)).thenReturn(Optional.of(taskExecution));
		
		final ResultFile resultFile = mock();
		
		taskExecutionService.addResultFile(id, resultFile);
		
		verify(taskExecution).addResultFile(resultFile);
		verify(taskExecutionRepository).save(taskExecution);
	}
	
	@Test
	void shouldSaveAll() {
		final List<TaskExecution> taskExecutions = mock();
		when(taskExecutionRepository.saveAll(taskExecutions)).thenReturn(taskExecutions);
		
		final List<TaskExecution> result = taskExecutionService.saveAll(taskExecutions);
		
		assertSame(taskExecutions, result);
	}
}