package com.beluga.model.job_definition;

import com.beluga.execution.model.task.TaskExecution;
import org.instancio.Instancio;
import org.junit.jupiter.api.Test;

import java.util.List;

import static com.beluga.execution.model.task.TaskStatus.COMPLETED;
import static com.beluga.execution.model.task.TaskStatus.PAUSED;
import static org.instancio.Select.field;
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
	
	@Test
	void shouldGetTasksByStatus() {
		final TaskExecution completedTask = Instancio.of(TaskExecution.class)
				.set(field(TaskExecution::getStatus), COMPLETED)
				.create();
		final TaskExecution pausedTask = Instancio.of(TaskExecution.class)
				.set(field(TaskExecution::getStatus), PAUSED)
				.create();
		
		final JobExecution jobExecution = Instancio.of(JobExecution.class)
				.set(field(JobExecution::getTasks), List.of(
						completedTask,
						pausedTask
				)).create();
		
		final List<TaskExecution> result = jobExecution.getTasksByStatus(PAUSED);
		
		assertEquals(1, result.size());
		assertTrue(result.contains(pausedTask));
	}
}