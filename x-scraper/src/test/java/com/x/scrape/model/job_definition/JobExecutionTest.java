package com.x.scrape.model.job_definition;

import com.x.scrape.model.task.TaskExecution;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class JobExecutionTest {
	
	@Test
	void shouldAddTask() {
		final JobExecution jobExecution = new JobExecution();
		final TaskExecution taskExecution = new TaskExecution();
		
		jobExecution.addTask(taskExecution);
		
		assertSame(jobExecution, taskExecution.getJobExecution());
		assertEquals(1, jobExecution.getTasks().size());
		assertTrue(jobExecution.getTasks().contains(taskExecution));
	}
	
}