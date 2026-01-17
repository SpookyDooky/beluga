package com.x.scrape.model.task;

import com.x.scrape.model.result.ResultFile;
import jakarta.persistence.EntityNotFoundException;
import org.instancio.Instancio;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TaskExecutionTest {
	
	@Test
	void shouldAddResultFile() {
		final TaskExecution taskExecution = new TaskExecution();
		final ResultFile resultFile = new ResultFile();
		
		taskExecution.addResultFile(resultFile);
		
		assertTrue(taskExecution.getResultFiles().contains(resultFile));
		assertSame(taskExecution, resultFile.getTaskExecution());
	}
	
	@Test
	void shouldGetResultFileByFileName() {
		final TaskExecution taskExecution = Instancio.create(TaskExecution.class);
		final ResultFile resultFile = taskExecution.getResultFiles()
				.getFirst();
		
		final ResultFile result = taskExecution.getResultFileByFileName(resultFile.getFileName());
		
		assertSame(resultFile, result);
	}
	
	@Test
	void shouldThrowEntityNotFoundExceptionForGetResultFileByFileName() {
		final TaskExecution taskExecution = Instancio.create(TaskExecution.class);
		
		assertThrows(EntityNotFoundException.class, () -> taskExecution.getResultFileByFileName(""));
	}
	
}