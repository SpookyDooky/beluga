package com.x.scrape.model.task;

import com.x.scrape.model.result.ResultFile;
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
	
}